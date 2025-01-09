package com.example.connect.domain.schedule.dto;

import com.example.connect.domain.schedule.entity.Schedule;
import com.example.connect.domain.schedulesubcategory.dto.ContentDescriptionDto;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class ScheduleServiceDto {

    private final LocalDate date;
    private final String title;
    private final String details;
    private final List<ContentDescriptionDto> contents;
    private final Long userId;
    private final String address;
    private final Double latitude;
    private final Double longitude;

    public ScheduleServiceDto(LocalDate date, String title, String details, List<ContentDescriptionDto> contents, Long userId, String address, Double latitude, Double longitude) {
        this.date = date;
        this.title = title;
        this.details = details;
        this.contents = contents;
        this.userId = userId;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Schedule toEntity() {
        return new Schedule(
                this.date,
                this.title,
                this.details,
                this.address,
                this.latitude,
                this.longitude
        );
    }
}
