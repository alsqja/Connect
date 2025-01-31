package com.example.connect.domain.coupon.repository;

import com.example.connect.domain.coupon.dto.CouponResDto;
import com.example.connect.global.enums.CouponFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomCouponRepository {
    Page<CouponResDto> findByOrderByCreatedAtDesc(CouponFilter filter, Pageable pageable);
}
