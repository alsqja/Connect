package com.example.connect.domain.report.service;

import com.example.connect.domain.report.entity.Report;
import com.example.connect.domain.report.repository.ReportRepository;
import com.example.connect.global.enums.UserStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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
}
