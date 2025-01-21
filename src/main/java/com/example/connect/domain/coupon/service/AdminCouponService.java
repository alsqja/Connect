package com.example.connect.domain.coupon.service;

import com.example.connect.domain.coupon.dto.AdminCouponReqDto;
import com.example.connect.domain.coupon.dto.CouponResDto;
import com.example.connect.domain.coupon.entity.Coupon;
import com.example.connect.domain.coupon.repository.CouponRepository;
import com.example.connect.global.error.errorcode.ErrorCode;
import com.example.connect.global.error.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AdminCouponService {
    private final CouponRepository couponRepository;

    public CouponResDto createCoupon(AdminCouponReqDto adminCouponReqDto) {
        if (
                adminCouponReqDto.getExpiredDate().compareTo(adminCouponReqDto.getOpenDate().toLocalDate()) >= 0
                        || adminCouponReqDto.getOpenDate().isBefore(LocalDateTime.now())
        ) {
            throw new BadRequestException(ErrorCode.BAD_REQUEST);
        }

        Coupon coupon = new Coupon(
                adminCouponReqDto.getName(),
                adminCouponReqDto.getDescription(),
                adminCouponReqDto.getCount(),
                adminCouponReqDto.getAmount(),
                adminCouponReqDto.getExpiredDate(),
                adminCouponReqDto.getOpenDate()
        );

        couponRepository.save(coupon);

        return new CouponResDto(coupon);
    }
}
