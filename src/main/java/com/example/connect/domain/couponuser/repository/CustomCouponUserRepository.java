package com.example.connect.domain.couponuser.repository;

import com.example.connect.domain.couponuser.dto.CouponUserResDto;
import com.example.connect.global.enums.CouponUserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomCouponUserRepository {
    Page<CouponUserResDto> findCouponUserByUserId(Long userId, CouponUserStatus status, Pageable pageable);
}
