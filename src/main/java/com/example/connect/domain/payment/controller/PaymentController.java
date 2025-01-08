package com.example.connect.domain.payment.controller;

import com.example.connect.domain.payment.dto.PaymentCancelReqDto;
import com.example.connect.domain.payment.dto.PaymentReqDto;
import com.example.connect.domain.payment.dto.PaymentResDto;
import com.example.connect.domain.payment.service.PaymentService;
import com.example.connect.domain.user.dto.RedisUserDto;
import com.example.connect.global.config.auth.UserDetailsImpl;
import com.example.connect.global.enums.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.ErrorResponseException;
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
    public ResponseEntity<PaymentResDto> createPayment(
            @RequestBody PaymentReqDto paymentReqDto,
            Authentication authentication
    ) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        RedisUserDto me = userDetails.getUser();

        PaymentResDto paymentResDto = paymentService.createPayment(paymentReqDto, me.getId());

        return ResponseEntity.ok(paymentResDto);
    }

    /**
     * 결제 취소(관리자만 가능)
     */
    @PostMapping("/cancel")
    public ResponseEntity<PaymentResDto> cancelPayment(
            @RequestBody PaymentCancelReqDto paymentCancelReqDto,
            Authentication authentication
    ) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        UserRole myRole = userDetails.getUser().getRole();
        PaymentResDto paymentResDto;

        if (myRole.equals(UserRole.ADMIN)) {
            paymentResDto = paymentService.cancelPayment(paymentCancelReqDto.getPaymentId(), paymentCancelReqDto.getAmount(), paymentCancelReqDto.getReason());
        } else {
            throw new ErrorResponseException(HttpStatus.FORBIDDEN);
        }

        return ResponseEntity.ok(paymentResDto);
    }
}
