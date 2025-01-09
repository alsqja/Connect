package com.example.connect.domain.payment.dto;

import com.example.connect.global.enums.PaymentStatus;
import com.example.connect.global.enums.PaymentType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Getter
@RequiredArgsConstructor
public class PaymentReqDto {
    private final String payUid;
    private final String portoneUid;
    private final BigDecimal amount;
    private final String details;
    private final PaymentType type;
    private final PaymentStatus status;
}
