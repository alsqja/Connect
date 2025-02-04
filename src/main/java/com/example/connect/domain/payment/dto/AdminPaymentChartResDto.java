package com.example.connect.domain.payment.dto;

import com.example.connect.global.common.dto.BaseDtoType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public class AdminPaymentChartResDto implements BaseDtoType {
    private final LocalDate date;
    private final BigDecimal sales;
    private final BigDecimal cumulativeSales;
}
