package com.example.connect.domain.payment.controller;

import com.example.connect.domain.payment.dto.PaymentCancelReqDto;
import com.example.connect.domain.payment.dto.PaymentReqDto;
import com.example.connect.domain.payment.dto.PaymentResDto;
import com.example.connect.domain.payment.service.PaymentService;
import com.example.connect.domain.user.dto.RedisUserDto;
import com.example.connect.global.common.dto.CommonResDto;
import com.example.connect.global.config.auth.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class PaymentController {
    private final PaymentService paymentService;

    /**
     * 결제 저장
     */
    @PostMapping
    public ResponseEntity<CommonResDto<PaymentResDto>> createPayment(
            @RequestBody PaymentReqDto paymentReqDto,
            Authentication authentication
    ) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        RedisUserDto me = userDetails.getUser();

        PaymentResDto paymentResDto = paymentService.createPayment(paymentReqDto, me.getId());

        return new ResponseEntity<>(new CommonResDto<>("결제 생성 완료.", paymentResDto), HttpStatus.CREATED);
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
}
