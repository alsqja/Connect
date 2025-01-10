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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

        if (reportRepository.countAllByToUser(toUser) >= 5) {
            toUser.updateStatus(UserStatus.REJECTED);
        }

        return new ReportResDto(report);
    }
}
