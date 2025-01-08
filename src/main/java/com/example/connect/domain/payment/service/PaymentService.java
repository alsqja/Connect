package com.example.connect.domain.payment.service;

import com.example.connect.domain.payment.dto.PaidPaymentResDto;
import com.example.connect.domain.payment.dto.PaymentReqDto;
import com.example.connect.domain.payment.dto.PaymentResDto;
import com.example.connect.domain.payment.entity.Payment;
import com.example.connect.domain.payment.repository.PaymentRepository;
import com.example.connect.domain.point.entity.Point;
import com.example.connect.domain.point.repository.PointRepository;
import com.example.connect.domain.user.entity.User;
import com.example.connect.domain.user.repository.UserRepository;
import com.example.connect.global.enums.PaymentStatus;
import com.example.connect.global.error.errorcode.ErrorCode;
import com.example.connect.global.error.exception.BadRequestException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;
    private final PointRepository pointRepository;
    private final PortoneService portoneService;
    private final RestTemplate restTemplate;

    /**
     * 결제 추가
     */
    @Transactional
    public PaymentResDto createPayment(
            PaymentReqDto paymentReqDto,
            Long userId
    ) {
        User user = userRepository.findById(userId).orElseThrow();
        HttpHeaders headers = portoneService.makeHeaders();

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
        ResponseEntity<PaidPaymentResDto> responseEntity = getPaymentData(headers, paymentReqDto.getPayUid());
        BigDecimal totalAmount = BigDecimal.valueOf(responseEntity.getBody().getAmount().getTotal());

        // 요청금액과 결제 금액이 같은지, 결제에 성공한 상태인지 검증
        if (payment.getAmount().compareTo(totalAmount) == 0 && responseEntity.getBody().getStatus().equals("PAID")) {
            payment = paymentRepository.save(payment);
            Point point = new Point(paymentReqDto.getAmount().divide(new BigDecimal(10)), user, payment);

            pointRepository.save(point);
        } else {
            cancelPayment(payment.getId(), payment.getAmount(), "결제 금액 오류");

            throw new BadRequestException(ErrorCode.BAD_REQUEST);
        }

        PaymentResDto paymentResDto = new PaymentResDto(
                payment.getPayUid(),
                payment.getPortoneUid(),
                payment.getAmount(),
                payment.getDetails(),
                payment.getType(),
                payment.getPayStatus()
        );

        return paymentResDto;
    }

    /**
     * 결제 취소
     */
    @Transactional
    public PaymentResDto cancelPayment(Long paymentId, BigDecimal amount, String reason) {
        Payment payment = paymentRepository.findById(paymentId).orElseThrow();
        HttpHeaders headers = portoneService.makeHeaders();

        // 결제 정보 단건 조회
        ResponseEntity<PaidPaymentResDto> responseEntity = getPaymentData(headers, payment.getPayUid());
        BigDecimal totalAmount = BigDecimal.valueOf(responseEntity.getBody().getAmount().getTotal());

        // TODO: point 구현 완료 후 검증 로직 추가 (결제 금액중 포인트 사용분이 있을경우 결제 취소 불가)
        // 환불 요청금액이 DB 와 같은지, 포트원에 넘어간 금액과 같은지 검증
        if (payment.getAmount().compareTo(amount) != 0 || totalAmount.compareTo(amount) != 0) {
            throw new BadRequestException(ErrorCode.BAD_REQUEST);
        }

        if (payment.getPayStatus().equals(PaymentStatus.PAID)) {
            // 결제 취소
            String url = "https://api.portone.io/payments/" + payment.getPayUid() + "/cancel";
            HttpEntity<String> request = new HttpEntity<>("{\"reason\" : \"" + reason + "\"}", headers);

            payment.updatePayStatus(PaymentStatus.CANCELLED, reason);
            restTemplate.exchange(url, HttpMethod.POST, request, String.class);

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

    // 검증을 위한 결제 단건 조회
    public ResponseEntity<PaidPaymentResDto> getPaymentData(HttpHeaders headers, String paymentId) {
        return restTemplate.exchange("https://api.portone.io/payments/" + paymentId, HttpMethod.GET, new HttpEntity<>(headers), PaidPaymentResDto.class);
    }
}
