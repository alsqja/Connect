package com.example.connect.domain.coupon.service;

import com.example.connect.domain.coupon.dto.CouponListResDto;
import com.example.connect.domain.coupon.dto.CouponResDto;
import com.example.connect.domain.coupon.entity.Coupon;
import com.example.connect.domain.coupon.repository.CouponRepository;
import com.example.connect.domain.couponuser.dto.CouponUserResDto;
import com.example.connect.domain.couponuser.dto.CouponUserUseResDto;
import com.example.connect.domain.couponuser.entity.CouponUser;
import com.example.connect.domain.couponuser.repository.CouponUserRepository;
import com.example.connect.domain.schedule.entity.Schedule;
import com.example.connect.domain.schedule.repository.ScheduleRepository;
import com.example.connect.domain.user.entity.User;
import com.example.connect.domain.user.repository.UserRepository;
import com.example.connect.global.enums.CouponUserStatus;
import com.example.connect.global.error.errorcode.ErrorCode;
import com.example.connect.global.error.exception.BadRequestException;
import com.example.connect.global.error.exception.UnAuthorizedException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CouponService {
    private final CouponRepository couponRepository;
    private final CouponUserRepository couponUserRepository;
    private final UserRepository userRepository;
    private final ScheduleRepository scheduleRepository;

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

    @Transactional
    public CouponUserResDto issueCouponUser(Long id, Long userId) {
        Coupon coupon = couponRepository.findByIdOrElseThrow(id);
        User user = userRepository.findByIdOrElseThrow(userId);

        Long couponCount = couponUserRepository.countCouponUseId(id);
        boolean alreadyHave = couponUserRepository.existsByCouponIdAndUserId(id, userId);

        if (
                couponCount >= coupon.getCount()
                        || coupon.getOpenDate().isAfter(LocalDateTime.now())
                        || !coupon.getExpiredDate().isAfter(LocalDate.now())
        ) {
            throw new BadRequestException(ErrorCode.NOT_ISSUE_COUPON);
        }

        if (alreadyHave) {
            throw new BadRequestException(ErrorCode.ALREADY_HAVE_COUPON);
        }

        CouponUser couponUser = new CouponUser(
                coupon.getExpiredDate(),
                CouponUserStatus.UNUSED,
                user,
                coupon
        );

        couponUser = couponUserRepository.save(couponUser);

        CouponUserResDto couponUserResDto = new CouponUserResDto(
                couponUser.getId(),
                user.getId(),
                coupon.getId(),
                coupon.getDescription(),
                coupon.getExpiredDate(),
                couponUser.getCreatedAt(),
                couponUser.getUpdatedAt()
        );
        return couponUserResDto;
    }

    @Transactional
    public CouponUserUseResDto useCoupon(Long id, Long scheduleId, Long userId) {
        CouponUser couponUser = couponUserRepository.findByIdOrElseThrow(id);
        Schedule schedule = scheduleRepository.findByIdOrElseThrow(scheduleId);

        if (!Objects.equals(userId, couponUser.getUser().getId())
                || !Objects.equals(userId, schedule.getUser().getId())
        ) {
            throw new UnAuthorizedException(ErrorCode.UNAUTHORIZED_USER);
        }

        if (couponUser.getStatus().equals(CouponUserStatus.USED)) {
            throw new BadRequestException(ErrorCode.USED_COUPON);
        }

        Coupon coupon = couponRepository.findByIdOrElseThrow(couponUser.getUser().getId());

        schedule.decreaseCount(coupon.getAmount());
        couponUser.isUse();

        CouponUserUseResDto couponUserUseResDto = new CouponUserUseResDto(
                coupon.getAmount(),
                5 - schedule.getCount()
        );

        return couponUserUseResDto;
    }
}
