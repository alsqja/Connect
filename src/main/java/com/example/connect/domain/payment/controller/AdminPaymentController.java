package com.example.connect.domain.payment.controller;

import com.example.connect.domain.payment.dto.AdminPaymentChartResDto;
import com.example.connect.domain.payment.service.AdminPaymentService;
import com.example.connect.global.common.dto.CommonListResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/payments")
public class AdminPaymentController {
    private final AdminPaymentService adminPaymentService;

    @GetMapping("/daily-chart")
    public ResponseEntity<CommonListResDto<AdminPaymentChartResDto>> getPaymentsDaily(
            @RequestParam(defaultValue = "#{T(java.time.LocalDate).now().minusMonths(1)}") LocalDate startDate,
            @RequestParam(defaultValue = "#{T(java.time.LocalDate).now()}") LocalDate endDate
    ) {
        List<AdminPaymentChartResDto> result = adminPaymentService.getPaymentsDaily(startDate, endDate);

        return new ResponseEntity<>(new CommonListResDto<>("차트 조회 완료", result), HttpStatus.OK);
    }
}
