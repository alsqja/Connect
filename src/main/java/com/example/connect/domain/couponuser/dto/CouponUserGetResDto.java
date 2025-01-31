package com.example.connect.domain.couponuser.dto;

import com.example.connect.global.enums.CouponUserStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class CouponUserGetResDto {
    private final Long id;
    private final Long userId;
    private final Long couponId;
    private final String couponName;
    private final String couponDescription;
    private final LocalDate expiredDate;
    private final CouponUserStatus status;
    private final Integer amount;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
}
