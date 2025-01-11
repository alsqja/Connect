package com.example.connect.domain.schedule.dto;

import com.example.connect.domain.schedule.entity.Schedule;
import com.example.connect.global.common.dto.BaseDtoType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class ScheduleWithContentNameResDto implements BaseDtoType {

    private final Long id;
    private final LocalDate date;
    private final String title;
    private final String details;
    private final List<String> contentNames;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public ScheduleWithContentNameResDto(Schedule schedule, List<String> contentNames) {
        this.id = schedule.getId();
        this.date = schedule.getDate();
        this.title = schedule.getTitle();
        this.details = schedule.getDetails();
        this.contentNames = contentNames;
        this.createdAt = schedule.getCreatedAt();
        this.updatedAt = schedule.getUpdatedAt();
    }

    public ScheduleWithContentNameResDto(Schedule schedule) {
        this.id = schedule.getId();
        this.date = schedule.getDate();
        this.title = schedule.getTitle();
        this.details = schedule.getDetails();
        this.contentNames = schedule.getScheduleContents().stream().map(i -> i.getSubCategory().getName()).toList();
        this.createdAt = schedule.getCreatedAt();
        this.updatedAt = schedule.getUpdatedAt();
    }
}
