package com.example.connect.domain.coupon.controller;

import com.example.connect.domain.coupon.dto.CouponListResDto;
import com.example.connect.domain.coupon.dto.CouponResDto;
import com.example.connect.domain.coupon.dto.CouponUseDto;
import com.example.connect.domain.coupon.service.CouponService;
import com.example.connect.domain.coupon.service.RedissonIssueCoupon;
import com.example.connect.domain.couponuser.dto.CouponUserResDto;
import com.example.connect.domain.couponuser.dto.CouponUserUseResDto;
import com.example.connect.domain.user.dto.RedisUserDto;
import com.example.connect.global.common.dto.CommonResDto;
import com.example.connect.global.config.auth.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/coupons")
public class CouponController {
    private final CouponService couponService;
    private final RedissonIssueCoupon redissonIssueCoupon;

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

    @PostMapping("/{id}")
    public ResponseEntity<CommonResDto<CouponUserResDto>> issueCoupon(
            @PathVariable Long id,
            Authentication authentication
    ) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        RedisUserDto me = userDetails.getUser();

        CouponUserResDto couponResDto = redissonIssueCoupon.issueCoupon(id, me.getId());

        return new ResponseEntity<>(new CommonResDto<>("쿠폰 발급 완료.", couponResDto), HttpStatus.OK);
    }

    @PostMapping("/{id}/use")
    public ResponseEntity<CommonResDto<CouponUserUseResDto>> useCoupon(
            @PathVariable Long id,
            @RequestBody CouponUseDto couponUseDto,
            Authentication authentication
    ) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        RedisUserDto me = userDetails.getUser();

        CouponUserUseResDto result = couponService.useCoupon(id, couponUseDto.getScheduleId(), me.getId());

        return new ResponseEntity<>(new CommonResDto<>("쿠폰 사용 완료.", result), HttpStatus.OK);
    }
}
