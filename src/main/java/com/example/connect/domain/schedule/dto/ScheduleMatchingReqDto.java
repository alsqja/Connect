package com.example.connect.domain.schedule.dto;

import com.example.connect.global.enums.Gender;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ScheduleMatchingReqDto {

    private final Gender gender;
    private final int minusAge;
    private final int plusAge;
    private final double distance;
}
