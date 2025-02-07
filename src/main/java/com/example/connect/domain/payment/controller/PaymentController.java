package com.example.connect.domain.payment.controller;

import com.example.connect.domain.membership.service.MembershipService;
import com.example.connect.domain.payment.dto.EncryptReqDto;
import com.example.connect.domain.payment.dto.PaymentCancelReqDto;
import com.example.connect.domain.payment.dto.PaymentListResDto;
import com.example.connect.domain.payment.dto.PaymentReqDto;
import com.example.connect.domain.payment.dto.PaymentResDto;
import com.example.connect.domain.payment.service.PaymentService;
import com.example.connect.domain.point.service.PointService;
import com.example.connect.domain.user.dto.RedisUserDto;
import com.example.connect.global.common.dto.CommonResDto;
import com.example.connect.global.config.auth.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class PaymentController {
    private final PointService pointService;
    private final PaymentService paymentService;
    private final MembershipService membershipService;

    /**
     * 결제 저장 (point)
     */
    @PostMapping("/points")
    public ResponseEntity<CommonResDto<PaymentResDto>> createPaymentPoint(
            @RequestBody PaymentReqDto paymentReqDto,
            Authentication authentication
    ) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        RedisUserDto me = userDetails.getUser();

        PaymentResDto paymentResDto = pointService.createPaymentPoint(paymentReqDto, me.getId());

        return new ResponseEntity<>(new CommonResDto<>("포인트 결제 생성 완료.", paymentResDto), HttpStatus.CREATED);
    }

    /**
     * 결제 저장 (membership)
     */
    @PostMapping("/memberships")
    public ResponseEntity<CommonResDto<PaymentResDto>> createPaymentMembership(
            @RequestBody EncryptReqDto encryptReqDto,
            Authentication authentication
    ) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        RedisUserDto me = userDetails.getUser();

        PaymentResDto paymentResDto = membershipService.createPaymentMembership(encryptReqDto, me.getId());

        return new ResponseEntity<>(new CommonResDto<>("구독 결제 생성 완료.", paymentResDto), HttpStatus.CREATED);
    }

    /**
     * 결제 취소(관리자만 가능)
     */
    @PostMapping("/cancel")
    public ResponseEntity<CommonResDto<PaymentResDto>> cancelPayment(
            @RequestBody PaymentCancelReqDto paymentCancelReqDto
    ) {
        PaymentResDto paymentResDto = paymentService.cancelPayment(paymentCancelReqDto.getPaymentId(), paymentCancelReqDto.getAmount(), paymentCancelReqDto.getReason());

        return new ResponseEntity<>(new CommonResDto<>("결제 취소 완료.", paymentResDto), HttpStatus.OK);
    }

    /**
     * 결제 내역 조회
     */
    @GetMapping
    public ResponseEntity<CommonResDto<PaymentListResDto>> getPayment(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String payType,
            Authentication authentication
    ) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        RedisUserDto me = userDetails.getUser();

        PaymentListResDto payment = paymentService.getAllPayments(me.getId(), me.getRole(), payType, page, size);

        return new ResponseEntity<>(new CommonResDto<>("결제 조회 완료.", payment), HttpStatus.OK);
    }
}
