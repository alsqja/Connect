package com.example.connect.domain.point.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PointUpdateReqDto {
    private final Long matchingCount;
    private final String details;
}
