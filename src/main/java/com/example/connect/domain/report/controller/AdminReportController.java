package com.example.connect.domain.report.controller;

import com.example.connect.domain.report.dto.AdminReportResDto;
import com.example.connect.domain.report.service.AdminReportService;
import com.example.connect.global.common.dto.CommonResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/reports")
@RequiredArgsConstructor
public class AdminReportController {

    private final AdminReportService adminReportService;

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReport(@PathVariable Long id) {

        adminReportService.deleteReport(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<CommonResDto<AdminReportResDto>> getAllReports(@RequestParam(defaultValue = "1") int page,
                                                                         @RequestParam(defaultValue = "10") int size,
                                                                         @RequestParam(required = false) Long toUserId) {

        AdminReportResDto results = adminReportService.getAllReports(page, size, toUserId);

        return new ResponseEntity<>(new CommonResDto<>("신고 내역 조회 완료.", results), HttpStatus.OK);
    }
}
