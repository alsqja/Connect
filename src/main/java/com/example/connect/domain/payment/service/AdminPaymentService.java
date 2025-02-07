package com.example.connect.domain.payment.service;

import com.example.connect.domain.payment.dto.AdminPaymentChartResDto;
import com.example.connect.domain.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminPaymentService {
    private final PaymentRepository paymentRepository;

    public List<AdminPaymentChartResDto> getPaymentsDaily(LocalDate startDate, LocalDate endDate) {
        List<Object[]> findData = paymentRepository.findAllByCreatedAtBetween(startDate.atStartOfDay(), endDate.atTime(23, 59, 59));
        List<AdminPaymentChartResDto> chartResDtos = new ArrayList<>();

        for (Object[] row : findData) {
            LocalDate date = ((Date) row[0]).toLocalDate();
            BigDecimal sales = (BigDecimal) row[1];
            BigDecimal total = (BigDecimal) row[2];
            chartResDtos.add(new AdminPaymentChartResDto(date, sales, total));
        }

        return chartResDtos;
    }
}
