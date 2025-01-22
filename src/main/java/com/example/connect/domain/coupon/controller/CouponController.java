package com.example.connect.domain.coupon.controller;

import com.example.connect.domain.coupon.dto.CouponListResDto;
import com.example.connect.domain.coupon.dto.CouponResDto;
import com.example.connect.domain.coupon.service.CouponService;
import com.example.connect.global.common.dto.CommonResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/coupons")
public class CouponController {
    private final CouponService couponService;

    @GetMapping
    public ResponseEntity<CommonResDto<CouponListResDto>> getAllCoupons(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        CouponListResDto result = couponService.getCouponList(page, size);

        return new ResponseEntity<>(new CommonResDto<>("쿠폰 조회 완료.", result), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonResDto<CouponResDto>> getCouponById(
            @PathVariable Long id
    ) {
        CouponResDto result = couponService.getCoupon(id);

        return new ResponseEntity<>(new CommonResDto<>("쿠폰 단건 조회 완료.", result), HttpStatus.OK);
    }
}
