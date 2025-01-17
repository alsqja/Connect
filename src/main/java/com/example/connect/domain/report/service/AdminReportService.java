package com.example.connect.domain.report.service;

import com.example.connect.domain.report.dto.AdminReportResDto;
import com.example.connect.domain.report.dto.ReportResDto;
import com.example.connect.domain.report.entity.Report;
import com.example.connect.domain.report.repository.ReportRepository;
import com.example.connect.global.enums.UserStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminReportService {

    private final ReportRepository reportRepository;

    @Transactional
    public void deleteReport(Long id) {

        Report report = reportRepository.findByIdOrElseThrow(id);

        reportRepository.delete(report);

        if (report.getToUser().getReportedCount() == 5) {
            report.getToUser().updateStatus(UserStatus.NORMAL);
        }

        report.getToUser().minusReportedCount();
    }

    public AdminReportResDto getAllReports(int page, int size, Long toUserId) {

        Pageable pageable = PageRequest.of(page -1, size);
        Page<Report> reportDetails;

        if (toUserId != null) {
            reportDetails = reportRepository.findAllByToUserId(toUserId, pageable);
        } else {
            reportDetails = reportRepository.findAll(pageable);
        }

        return new AdminReportResDto(page, size, reportDetails.getTotalElements(), reportDetails.getTotalPages(), reportDetails.getContent().stream().map(ReportResDto::new).toList());
    }
}
