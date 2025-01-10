package com.example.connect.domain.report.service;

import com.example.connect.domain.match.entity.Matching;
import com.example.connect.domain.match.repository.MatchingRepository;
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
            throw new BadRequestException(ErrorCode.BAD_REQUEST);
        }

        Matching matching = matchingRepository.findByIdOrElseThrow(matchingId);

        Report report = new Report(content, matching, fromUser, toUser);

        reportRepository.save(report);

        toUser.addReportedCount();

        if (toUser.getReportedCount() >= 5) {
            toUser.updateStatus(UserStatus.REJECTED);
        }

        return new ReportResDto(report);
    }

    @Transactional
    public void cancelReport(Long id, Long myId) {

        //신고 내역 조회
        Report report = reportRepository.findByIdOrElseThrow(id);

        //
        if (!Objects.equals(report.getFromUser().getId(), myId)) {
            throw new ForbiddenException(ErrorCode.FORBIDDEN_PERMISSION);
        }

        //createdAt 으로부터 6개월 이내의 신고내역인가?
        if (report.getCreatedAt().isBefore(LocalDateTime.now().minusMonths(6))) {
            throw new BadRequestException(ErrorCode.BAD_REQUEST);
        }

        //신고 취소
        reportRepository.delete(report);

        //5회째의 신고가 취소되었을 경우, rejected -> normal
        if (report.getToUser().getReportedCount() == 5) {
            report.getToUser().updateStatus(UserStatus.NORMAL);
        }

        report.getToUser().minusReportedCount();
    }
}
