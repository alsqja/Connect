package com.example.connect.domain.user.controller;

import com.example.connect.domain.coupon.service.CouponService;
import com.example.connect.domain.couponuser.dto.CouponUserListResDto;
import com.example.connect.domain.membership.service.MembershipService;
import com.example.connect.domain.point.service.PointService;
import com.example.connect.domain.user.dto.RedisUserDto;
import com.example.connect.domain.user.dto.UpdateUserReqDto;
import com.example.connect.domain.user.dto.UpdateUserServiceDto;
import com.example.connect.domain.user.dto.UserResDto;
import com.example.connect.domain.user.dto.UserSimpleResDto;
import com.example.connect.domain.user.service.UserService;
import com.example.connect.global.common.dto.CommonResDto;
import com.example.connect.global.config.auth.UserDetailsImpl;
import com.example.connect.global.enums.CouponUserStatus;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final PointService pointService;
    private final CouponService couponService;
    private final MembershipService membershipService;

    @GetMapping("/my")
    public ResponseEntity<CommonResDto<UserResDto>> findMe(Authentication authentication) {

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        RedisUserDto me = userDetails.getUser();

        BigDecimal totalRemainPoint = pointService.totalRemainPoint(me.getId());
        Boolean isActiveMembership = membershipService.membershipActive(me.getId());

        return new ResponseEntity<>(new CommonResDto<>("프로필 조회 완료", new UserResDto(me, totalRemainPoint, isActiveMembership)), HttpStatus.OK);
    }

    @PatchMapping("/my")
    public ResponseEntity<CommonResDto<UserResDto>> updateMe(
            @RequestBody UpdateUserReqDto dto,
            Authentication authentication
    ) {

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        RedisUserDto me = userDetails.getUser();

        UpdateUserServiceDto serviceDto = dto.toServiceDto(me);
        BigDecimal totalRemainPoint = pointService.totalRemainPoint(me.getId());
        RedisUserDto redisUserDto = userService.updateMe(serviceDto);
        Boolean isActiveMembership = membershipService.membershipActive(me.getId());

        UserResDto result = new UserResDto(redisUserDto, totalRemainPoint, isActiveMembership);

        return new ResponseEntity<>(new CommonResDto<>("프로필 수정 완료", result), HttpStatus.OK);
    }

    @DeleteMapping("/my")
    public ResponseEntity<Void> deleteMe(
            Authentication authentication,
            HttpServletRequest request,
            HttpServletResponse response
    ) {

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        RedisUserDto me = userDetails.getUser();

        userService.deleteUser(me);

        if (authentication.isAuthenticated()) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonResDto<UserSimpleResDto>> findById(
            @PathVariable Long id
    ) {

        UserSimpleResDto result = userService.findById(id);

        return new ResponseEntity<>(new CommonResDto<>("유저 조회 완료", result), HttpStatus.OK);
    }

    @GetMapping("/my/coupons")
    public ResponseEntity<CommonResDto<CouponUserListResDto>> findCouponById(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "ALL") CouponUserStatus status,
            Authentication authentication
    ) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        RedisUserDto me = userDetails.getUser();

        CouponUserListResDto result = couponService.getUserCoupon(page, size, me.getId(), status);

        return new ResponseEntity<>(new CommonResDto<>("쿠폰 조회 완료.", result), HttpStatus.OK);
    }
}
