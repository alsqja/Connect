package com.example.connect.domain.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AutoPaymentResDto {
    private final SchedulePayRes payment;

    @Getter
    @AllArgsConstructor
    public static class SchedulePayRes {
        private String pgTxId;
        private String paidAt;
    }
}
