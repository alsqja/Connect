package com.example.connect.domain.coupon.dto;

import com.example.connect.global.common.dto.BaseDtoType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class CouponListResDto implements BaseDtoType {
    private final int page;
    private final int size;
    private final long totalElements;
    private final int totalPages;
    private final List<CouponResDto> data;
}
