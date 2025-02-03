package com.example.connect.domain.coupon.dto;

import com.example.connect.domain.coupon.entity.Coupon;
import com.example.connect.global.common.dto.BaseDtoType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class CouponResDto implements BaseDtoType {
    private final Long id;
    private final String name;
    private final String description;
    private final Integer count;
    private final Integer amount;
    private final LocalDate expiredDate;
    private final LocalDateTime openDate;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public CouponResDto(Coupon coupon) {
        this.id = coupon.getId();
        this.name = coupon.getName();
        this.description = coupon.getDescription();
        this.count = coupon.getCount();
        this.amount = coupon.getAmount();
        this.expiredDate = coupon.getExpiredDate();
        this.openDate = coupon.getOpenDate();
        this.createdAt = coupon.getCreatedAt();
        this.updatedAt = coupon.getUpdatedAt();
    }
}
