package com.example.connect.domain.payment.dto;

import com.example.connect.domain.payment.entity.Payment;
import com.example.connect.global.enums.PaymentStatus;
import com.example.connect.global.enums.PaymentType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class PaymentGetResDto {
    private final String payUid;
    private final String portoneUid;
    private final BigDecimal amount;
    private final String details;
    private final PaymentType type;
    private final PaymentStatus status;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public PaymentGetResDto(Payment payment) {
        this.payUid = payment.getPayUid();
        this.portoneUid = payment.getPortoneUid();
        this.amount = payment.getAmount();
        this.details = payment.getDetails();
        this.type = payment.getType();
        this.status = payment.getPayStatus();
        this.createdAt = payment.getCreatedAt();
        this.updatedAt = payment.getUpdatedAt();
    }
}
