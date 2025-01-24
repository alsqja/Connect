package com.example.connect.domain.couponuser.dto;

import com.example.connect.global.common.dto.BaseDtoType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CouponUserUseResDto implements BaseDtoType {
    private final Integer couponAmount;
    private final Integer totalFreeCount;
}
