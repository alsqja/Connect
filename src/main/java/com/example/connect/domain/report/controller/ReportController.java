package com.example.connect.domain.report.controller;

import com.example.connect.domain.report.dto.MyReportResDto;
import com.example.connect.domain.report.dto.ReportReqDto;
import com.example.connect.domain.report.dto.ReportResDto;
import com.example.connect.domain.report.service.ReportService;
import com.example.connect.domain.user.dto.RedisUserDto;
import com.example.connect.global.common.dto.CommonResDto;
import com.example.connect.global.config.auth.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping
    public ResponseEntity<CommonResDto<ReportResDto>> createReport(@Valid @RequestBody ReportReqDto reportReqDto,
                                                                   Authentication authentication) {

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        RedisUserDto redisUserDto = userDetails.getUser();

        ReportResDto result = reportService.createReport(redisUserDto.getId(), reportReqDto.getToId(), reportReqDto.getMatchingId(), reportReqDto.getContent());

        return new ResponseEntity<>(new CommonResDto<>("신고 접수 완료.", result), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelReport(@PathVariable Long id, Authentication authentication) {

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        RedisUserDto redisUserDto = userDetails.getUser();

        reportService.cancelReport(id, redisUserDto.getId());

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<CommonResDto<MyReportResDto>> getMyReports(@RequestParam(defaultValue = "1") int page,
                                                                     @RequestParam(defaultValue = "10") int size,
                                                                     Authentication authentication) {

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        RedisUserDto redisUserDto = userDetails.getUser();

        MyReportResDto results = reportService.getMyReports(page, size, redisUserDto.getId());

        return new ResponseEntity<>(new CommonResDto<>("본인 신고 내역 조회 완료.", results), HttpStatus.OK);
    }
}
