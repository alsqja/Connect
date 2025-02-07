package com.example.connect.domain.review.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ReviewReqDto {

    private final Long toId;

    @NotNull
    @Min(1)
    @Max(5)
    private final Integer rate;
}
