package com.example.connect.domain.coupon.service;

import com.example.connect.domain.coupon.dto.CouponListResDto;
import com.example.connect.domain.coupon.dto.CouponResDto;
import com.example.connect.domain.coupon.entity.Coupon;
import com.example.connect.domain.coupon.repository.CouponRepository;
import com.example.connect.domain.couponuser.dto.CouponUserGetResDto;
import com.example.connect.domain.couponuser.dto.CouponUserListResDto;
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
import java.time.format.DateTimeFormatter;
import java.util.List;
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
                coupon.getName(),
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

    public CouponUserListResDto getUserCoupon(int page, int size, Long userId, CouponUserStatus status) {
        Pageable pageable = PageRequest.of(page - 1, size);

        Page<CouponUserGetResDto> couponUsersList = couponUserRepository.findCouponUserByUserId(userId, status, pageable);

        return new CouponUserListResDto(page, size, couponUsersList.getTotalElements(), couponUsersList.getTotalPages(), couponUsersList.getContent());
    }

    @Transactional
    public void expireCoupon() {
        List<Coupon> coupons = couponRepository.findByExpiredDateIsLessThanEqual(LocalDate.now());

        couponRepository.deleteAll(coupons);

        List<CouponUser> couponUsers = couponUserRepository.findByExpiredDateIsLessThanEqual(LocalDate.now());

        couponUserRepository.deleteAll(couponUsers);
    }

    @Transactional
    public void createBirthCouponUser() {
        String now = LocalDate.now().format(DateTimeFormatter.ofPattern("MMdd"));
        String birthYear = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy"));
        LocalDate expiredDate = LocalDate.now().plusDays(14);

        List<User> users = userRepository.findByBirth(now);
        Coupon coupon = couponRepository.findByName(birthYear + " 년 생일 쿠폰");

        for (User user : users) {
            CouponUser couponUser = new CouponUser(
                    expiredDate,
                    CouponUserStatus.UNUSED,
                    user,
                    coupon
            );

            couponUserRepository.save(couponUser);
        }
    }

    @Transactional
    public void createBirthCoupon() {
        LocalDate now = LocalDate.now();
        String birthYear = now.format(DateTimeFormatter.ofPattern("yyyy"));

        Coupon coupon = new Coupon(
                birthYear + " 년 생일 쿠폰",
                birthYear + " 생일을 축하드립니다!",
                Integer.MAX_VALUE,
                5,
                LocalDate.of(Integer.parseInt(birthYear), 12, 31).plusDays(14),
                LocalDateTime.of(Integer.parseInt(birthYear), 12, 31, 0, 0, 0).plusDays(14)
        );

        couponRepository.save(coupon);
    }
}
