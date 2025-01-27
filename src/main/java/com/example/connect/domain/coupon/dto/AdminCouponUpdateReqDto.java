package com.example.connect.domain.coupon.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class AdminCouponUpdateReqDto {
    private final String name;
    private final String description;
    private final Integer count;
    private final Integer amount;
    private final LocalDate expiredDate;
    private final LocalDateTime openDate;
    private final Boolean isDeleted;
}
