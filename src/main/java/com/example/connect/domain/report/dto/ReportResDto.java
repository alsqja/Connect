package com.example.connect.domain.report.dto;

import com.example.connect.domain.report.entity.Report;
import com.example.connect.global.common.dto.BaseDtoType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ReportResDto implements BaseDtoType {

    private final Long id;
    private final Long fromId;
    private final Long toId;
    private final Long matchingId;
    private final String content;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public ReportResDto(Report report) {
        this.id = report.getId();
        this.fromId = report.getFromUser().getId();
        this.toId = report.getToUser().getId();
        this.matchingId = report.getMatching().getId();
        this.content = report.getContent();
        this.createdAt = report.getCreatedAt();
        this.updatedAt = report.getUpdatedAt();
    }
}
