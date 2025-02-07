package com.example.connect.domain.schedule.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public class ScheduleUpdateServiceDto {

    private final LocalDate date;
    private final String title;
    private final String details;
    private final String address;
    private final Double latitude;
    private final Double longitude;
}
