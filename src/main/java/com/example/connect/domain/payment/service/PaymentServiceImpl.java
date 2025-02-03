package com.example.connect.domain.payment.service;

import com.example.connect.domain.payment.dto.AutoPaymentReqDto;
import com.example.connect.domain.payment.dto.AutoPaymentResDto;
import com.example.connect.domain.payment.dto.PaidPaymentResDto;
import com.example.connect.domain.payment.dto.PaymentGetResDto;
import com.example.connect.domain.payment.dto.PaymentListResDto;
import com.example.connect.domain.payment.dto.PaymentReqDto;
import com.example.connect.domain.payment.dto.PaymentResDto;
import com.example.connect.domain.payment.entity.Payment;
import com.example.connect.domain.payment.repository.PaymentRepository;
import com.example.connect.domain.point.entity.Point;
import com.example.connect.domain.point.repository.PointRepository;
import com.example.connect.domain.pointuse.entity.PointUse;
import com.example.connect.domain.pointuse.repository.PointUseRepository;
import com.example.connect.domain.user.entity.User;
import com.example.connect.global.enums.PaymentStatus;
import com.example.connect.global.enums.PaymentType;
import com.example.connect.global.enums.PointUseType;
import com.example.connect.global.enums.UserRole;
import com.example.connect.global.error.errorcode.ErrorCode;
import com.example.connect.global.error.exception.BadRequestException;
import com.example.connect.global.error.exception.ForbiddenException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final PointRepository pointRepository;
    private final PointUseRepository pointUseRepository;
    private final PortoneService portoneService;
    private final WebClient webClient;

    @Override
    @Transactional
    public Payment createPaymentCheck(
            PaymentReqDto paymentReqDto,
            User user
    ) {
        String token = portoneService.portoneToken();

        Payment payment = new Payment(
                paymentReqDto.getPayUid(),
                paymentReqDto.getPortoneUid(),
                paymentReqDto.getAmount(),
                paymentReqDto.getType(),
                paymentReqDto.getStatus(),
                paymentReqDto.getDetails(),
                user
        );

        // 결제 정보 단건 조회
        PaidPaymentResDto responseEntity = getPaymentData(paymentReqDto.getPayUid(), token);
        BigDecimal totalAmount = BigDecimal.valueOf(responseEntity.getAmount().getTotal());

        // 요청금액과 결제 금액이 같은지, 결제에 성공한 상태인지 검증
        if (payment.getAmount().compareTo(totalAmount) == 0 && responseEntity.getStatus().equals("PAID")) {
            return payment;
        } else {
            throw new BadRequestException(ErrorCode.BAD_REQUEST);
        }
    }

    /**
     * 결제 취소
     */
    @Override
    @Transactional
    public PaymentResDto cancelPayment(Long paymentId, BigDecimal amount, String reason) {
        Payment payment = paymentRepository.findById(paymentId).orElseThrow();
        String token = portoneService.portoneToken();

        if (payment.getType() == PaymentType.SUBSCRIBE) {
            throw new BadRequestException(ErrorCode.BAD_REQUEST);
        }

        if (pointUseRepository.isPointUse(paymentId)) {
            throw new BadRequestException(ErrorCode.PAYMENT_USE_POINT);
        }

        // 결제 정보 단건 조회
        PaidPaymentResDto responseEntity = getPaymentData(payment.getPayUid(), token);
        BigDecimal totalAmount = BigDecimal.valueOf(responseEntity.getAmount().getTotal());

        // 환불 요청금액이 DB 와 같은지, 포트원에 넘어간 금액과 같은지 검증
        if (payment.getAmount().compareTo(amount) != 0 || totalAmount.compareTo(amount) != 0) {
            throw new BadRequestException(ErrorCode.BAD_REQUEST);
        }

        if (payment.getPayStatus().equals(PaymentStatus.PAID)) {
            // 결제 취소
            String url = "/payments/" + payment.getPayUid() + "/cancel";
            Point point = pointRepository.findByPaymentId(paymentId);

            payment.updatePayStatus(PaymentStatus.CANCELLED, reason);
            point.pointUpdate(true);

            PointUse pointUse = new PointUse(
                    payment.getAmount().divide(new BigDecimal(10)),
                    BigDecimal.ZERO,
                    reason,
                    PointUseType.REFUND,
                    point
            );

            webClient.post()
                    .uri(url)
                    .header("Authorization", "Bearer " + token)
                    .body(Mono.just(Map.of("reason", reason)), Map.class)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();

            pointUseRepository.save(pointUse);

            PaymentResDto paymentResDto = new PaymentResDto(
                    payment.getPayUid(),
                    payment.getPortoneUid(),
                    payment.getAmount(),
                    payment.getDetails(),
                    payment.getType(),
                    payment.getPayStatus()
            );

            return paymentResDto;
        } else {
            throw new BadRequestException(ErrorCode.BAD_REQUEST);
        }
    }

    /**
     * 결제 조회
     * 1. 관리자 유저만 type을 통한 검색 사용
     * 2. 전체 검색의 경우 일반 유저 사용
     */
    @Override
    public PaymentListResDto getAllPayments(
            Long userId, UserRole userRole, String type, int page, int size
    ) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<PaymentGetResDto> payments;

        if (type != null && !type.isEmpty()) {
            if (userRole == UserRole.ADMIN) {
                payments = paymentRepository.findByType(PaymentType.valueOf(type), pageable);
            } else {
                throw new ForbiddenException(ErrorCode.FORBIDDEN_PERMISSION);
            }
        } else {
            if (userRole == UserRole.USER) {
                payments = paymentRepository.findByUserData(userId, pageable);
            } else {
                throw new ForbiddenException(ErrorCode.FORBIDDEN_PERMISSION);
            }
        }

        return new PaymentListResDto(page, size, payments.getTotalElements(), payments.getTotalPages(), payments.getContent());
    }

    /**
     * 검증을 위한 결제 단건 조회
     */
    private PaidPaymentResDto getPaymentData(String paymentId, String token) {
        return webClient.get()
                .uri("/payments/" + paymentId)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(PaidPaymentResDto.class)
                .block();
    }

    /**
     * 수기 카드 결제
     */
    @Override
    public AutoPaymentResDto cardPayment(String paymentId, AutoPaymentReqDto autoPaymentReqDto) {
        String token = portoneService.portoneToken();

        return webClient.post()
                .uri("https://api.portone.io/payments/" + paymentId + "/instant")
                .header("Authorization", "Bearer " + token)
                .bodyValue(autoPaymentReqDto)
                .retrieve()
                .bodyToMono(AutoPaymentResDto.class)
                .block();
    }

    @Override
    public void cancelPaymentPortone(String paymentUid, String reason) {
        String url = "/payments/" + paymentUid + "/cancel";
        String token = portoneService.portoneToken();

        webClient.post()
                .uri(url)
                .header("Authorization", "Bearer " + token)
                .body(Mono.just(Map.of("reason", reason)), Map.class)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
}
