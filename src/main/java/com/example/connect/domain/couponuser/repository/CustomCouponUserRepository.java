package com.example.connect.domain.couponuser.repository;

import com.example.connect.domain.couponuser.dto.CouponUserGetResDto;
import com.example.connect.global.enums.CouponUserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomCouponUserRepository {
    Page<CouponUserGetResDto> findCouponUserByUserId(Long userId, CouponUserStatus status, Pageable pageable);
}
