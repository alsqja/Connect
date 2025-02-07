package com.example.connect.domain.payment.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Getter
@RequiredArgsConstructor
public class PaymentCancelReqDto {
    private final Long paymentId;
    private final BigDecimal amount;
    private final String reason;
}
