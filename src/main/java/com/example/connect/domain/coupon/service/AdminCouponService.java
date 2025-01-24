package com.example.connect.domain.coupon.service;

import com.example.connect.domain.coupon.dto.AdminCouponReqDto;
import com.example.connect.domain.coupon.dto.AdminCouponUpdateReqDto;
import com.example.connect.domain.coupon.dto.CouponResDto;
import com.example.connect.domain.coupon.entity.Coupon;
import com.example.connect.domain.coupon.repository.CouponRepository;
import com.example.connect.global.error.errorcode.ErrorCode;
import com.example.connect.global.error.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AdminCouponService {
    private final CouponRepository couponRepository;

    public CouponResDto createCoupon(AdminCouponReqDto adminCouponReqDto) {
        dateCheck(adminCouponReqDto.getOpenDate(), adminCouponReqDto.getExpiredDate());

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

    @Transactional
    public CouponResDto updateCoupon(Long couponId, AdminCouponUpdateReqDto adminCouponUpdateReqDto) {
        Coupon coupon = couponRepository.findByIdOrElseThrow(couponId);

        if (adminCouponUpdateReqDto.getExpiredDate() != null && adminCouponUpdateReqDto.getOpenDate() != null) {
            dateCheck(adminCouponUpdateReqDto.getOpenDate(), adminCouponUpdateReqDto.getExpiredDate());
        }

        if (adminCouponUpdateReqDto.getOpenDate() != null && adminCouponUpdateReqDto.getExpiredDate() == null) {
            dateCheck(adminCouponUpdateReqDto.getOpenDate(), coupon.getExpiredDate());
        }

        if (adminCouponUpdateReqDto.getOpenDate() == null && adminCouponUpdateReqDto.getExpiredDate() != null) {
            dateCheck(coupon.getOpenDate(), adminCouponUpdateReqDto.getExpiredDate());
        }

        coupon.updateCoupon(
                adminCouponUpdateReqDto.getName(),
                adminCouponUpdateReqDto.getDescription(),
                adminCouponUpdateReqDto.getCount(),
                adminCouponUpdateReqDto.getAmount(),
                adminCouponUpdateReqDto.getExpiredDate(),
                adminCouponUpdateReqDto.getOpenDate(),
                adminCouponUpdateReqDto.getIsDeleted()
        );

        CouponResDto couponResDto = new CouponResDto(coupon);

        return couponResDto;
    }

    private void dateCheck(LocalDateTime openDate, LocalDate expiredDate) {
        if (expiredDate.compareTo(openDate.toLocalDate()) <= 0 || openDate.isBefore(LocalDateTime.now())) {
            throw new BadRequestException(ErrorCode.BAD_REQUEST);
        }
    }
}
