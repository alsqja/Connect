package com.example.connect.domain.coupon.controller;

import com.example.connect.domain.coupon.dto.AdminCouponReqDto;
import com.example.connect.domain.coupon.dto.AdminCouponUpdateReqDto;
import com.example.connect.domain.coupon.dto.CouponResDto;
import com.example.connect.domain.coupon.service.AdminCouponService;
import com.example.connect.global.common.dto.CommonResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/coupons")
public class AdminCouponController {
    private final AdminCouponService adminCouponService;

    @PostMapping
    public ResponseEntity<CommonResDto<CouponResDto>> createCoupon(
            @RequestBody AdminCouponReqDto adminCouponReqDto
    ) {
        CouponResDto result = adminCouponService.createCoupon(adminCouponReqDto);

        return new ResponseEntity<>(new CommonResDto<>("쿠폰 생성 완료.", result), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CommonResDto<CouponResDto>> updateCoupon(
            @PathVariable Long id,
            @RequestBody AdminCouponUpdateReqDto adminCouponUpdateReqDto
    ) {
        CouponResDto result = adminCouponService.updateCoupon(id, adminCouponUpdateReqDto);

        return new ResponseEntity<>(new CommonResDto<>("쿠폰 수정 완료.", result), HttpStatus.OK);
    }
}
