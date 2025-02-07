package com.example.connect.domain.couponuser.dto;

import com.example.connect.global.common.dto.BaseDtoType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class CouponUserResDto implements BaseDtoType {
    private final Long id;
    private final Long userId;
    private final Long couponId;
    private final String couponName;
    private final String couponDescription;
    private final LocalDate expiredDate;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
}
