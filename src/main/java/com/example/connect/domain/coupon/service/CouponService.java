package com.example.connect.domain.coupon.service;

import com.example.connect.domain.coupon.dto.CouponListResDto;
import com.example.connect.domain.coupon.dto.CouponResDto;
import com.example.connect.domain.coupon.entity.Coupon;
import com.example.connect.domain.coupon.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponService {
    private final CouponRepository couponRepository;

    public CouponListResDto getCouponList(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<CouponResDto> coupons = couponRepository.findByOrderByCreatedAtDesc(pageable);

        CouponListResDto resDto = new CouponListResDto(
                page, size, coupons.getTotalElements(), coupons.getTotalPages(), coupons.getContent()
        );

        return resDto;
    }

    public CouponResDto getCoupon(Long id) {
        Coupon coupon = couponRepository.findByIdOrElseThrow(id);

        return new CouponResDto(coupon);
    }
}
