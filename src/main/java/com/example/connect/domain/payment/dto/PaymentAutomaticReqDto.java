package com.example.connect.domain.payment.dto;

import com.example.connect.global.enums.PaymentStatus;
import com.example.connect.global.enums.PaymentType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class PaymentAutomaticReqDto {
    private String payUid;
    private BigDecimal amount;
    private String details;
    private PaymentType type;
    private PaymentStatus status;
    private String cardNumber;
    private String expiredYear;
    private String expiredMonth;
    private String cardPassword;
    private String birth;
}
