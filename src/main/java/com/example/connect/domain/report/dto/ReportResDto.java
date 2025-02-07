package com.example.connect.domain.report.dto;

import com.example.connect.domain.report.entity.Report;
import com.example.connect.domain.schedule.entity.Schedule;
import com.example.connect.global.common.dto.BaseDtoType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ReportResDto implements BaseDtoType {

    private final Long id;
    private final Long fromId;
    private final Long toId;
    private final String toName;
    private final Long scheduleId;
    private final String scheduleTitle;
    private final LocalDate scheduleDate;
    private final Long matchingId;
    private final String content;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public ReportResDto(Report report) {
        Schedule mySchedule = report.getMatching().getToSchedule().getUser().getId().equals(report.getFromUser().getId()) ? report.getMatching().getToSchedule() : report.getMatching().getFromSchedule();
        this.id = report.getId();
        this.fromId = report.getFromUser().getId();
        this.toId = report.getToUser().getId();
        this.scheduleId = mySchedule.getId();
        this.scheduleTitle = mySchedule.getTitle();
        this.scheduleDate = mySchedule.getDate();
        this.toName = report.getToUser().getName();
        this.matchingId = report.getMatching().getId();
        this.content = report.getContent();
        this.createdAt = report.getCreatedAt();
        this.updatedAt = report.getUpdatedAt();
    }
}
