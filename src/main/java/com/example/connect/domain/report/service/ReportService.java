package com.example.connect.domain.report.service;

import com.example.connect.domain.match.entity.Matching;
import com.example.connect.domain.match.repository.MatchingRepository;
import com.example.connect.domain.report.dto.MyReportResDto;
import com.example.connect.domain.report.dto.ReportResDto;
import com.example.connect.domain.report.entity.Report;
import com.example.connect.domain.report.repository.ReportRepository;
import com.example.connect.domain.user.entity.User;
import com.example.connect.domain.user.repository.UserRepository;
import com.example.connect.global.enums.UserStatus;
import com.example.connect.global.error.errorcode.ErrorCode;
import com.example.connect.global.error.exception.BadRequestException;
import com.example.connect.global.error.exception.ForbiddenException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final MatchingRepository matchingRepository;

    @Transactional
    public ReportResDto createReport(Long fromId, Long toId, Long matchingId, String content) {

        if (Objects.equals(fromId, toId)) {
            throw new BadRequestException(ErrorCode.BAD_REQUEST);
        }

        User fromUser = userRepository.findByIdOrElseThrow(fromId);
        User toUser = userRepository.findByIdOrElseThrow(toId);

        if (reportRepository.existsByFromUserAndToUser(fromUser, toUser)) {
            throw new BadRequestException(ErrorCode.ALREADY_REPORTED);
        }

        Matching matching = matchingRepository.findByIdOrElseThrow(matchingId);

        Report report = new Report(content, matching, fromUser, toUser);

        reportRepository.save(report);

        toUser.addReportedCount();

        if (toUser.getReportedCount() == 5) {
            toUser.updateStatus(UserStatus.REJECTED);
        }

        return new ReportResDto(report);
    }

    @Transactional
    public void cancelReport(Long id, Long myId) {

        Report report = reportRepository.findByIdOrElseThrow(id);

        if (!Objects.equals(report.getFromUser().getId(), myId)) {
            throw new ForbiddenException(ErrorCode.FORBIDDEN_PERMISSION);
        }

        if (report.getCreatedAt().isBefore(LocalDateTime.now().minusMonths(6))) {
            throw new BadRequestException(ErrorCode.BAD_REQUEST);
        }

        reportRepository.delete(report);

        if (report.getToUser().getReportedCount() == 5) {
            report.getToUser().updateStatus(UserStatus.NORMAL);
        }

        report.getToUser().minusReportedCount();
    }

    public MyReportResDto getMyReports(int page, int size, Long userId) {

        Pageable pageable = PageRequest.of(page -1, size);
        Page<Report> reportDetails = reportRepository.findByFromUserId(userId, pageable);

        return new MyReportResDto(page, size, reportDetails.getTotalElements(), reportDetails.getTotalPages(), reportDetails.getContent().stream().map(ReportResDto::new).toList());
    }
}
