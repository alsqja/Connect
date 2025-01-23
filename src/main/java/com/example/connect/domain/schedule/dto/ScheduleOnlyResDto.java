package com.example.connect.domain.schedule.dto;

import com.example.connect.domain.schedule.entity.Schedule;
import com.example.connect.global.common.dto.BaseDtoType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Getter
public class ScheduleOnlyResDto implements BaseDtoType {

    private final Long id;
    private final LocalDate date;
    private final String title;
    private final String details;
    private final int count;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public ScheduleOnlyResDto(Schedule schedule) {
        this.id = schedule.getId();
        this.date = schedule.getDate();
        this.title = schedule.getTitle();
        this.details = schedule.getDetails();
        this.count = schedule.getCount();
        this.createdAt = schedule.getCreatedAt();
        this.updatedAt = schedule.getUpdatedAt();
    }
}
