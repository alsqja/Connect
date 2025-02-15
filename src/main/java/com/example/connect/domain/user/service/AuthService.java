package com.example.connect.domain.user.service;

import com.example.connect.domain.coupon.entity.Coupon;
import com.example.connect.domain.coupon.repository.CouponRepository;
import com.example.connect.domain.couponuser.entity.CouponUser;
import com.example.connect.domain.couponuser.repository.CouponUserRepository;
import com.example.connect.domain.membership.entity.Membership;
import com.example.connect.domain.membership.repository.MembershipRepository;
import com.example.connect.domain.user.dto.RedisUserDto;
import com.example.connect.domain.user.dto.SignupResDto;
import com.example.connect.domain.user.dto.SignupServiceDto;
import com.example.connect.domain.user.dto.UserTokenResDto;
import com.example.connect.domain.user.entity.User;
import com.example.connect.domain.user.repository.RedisEmailRepository;
import com.example.connect.domain.user.repository.UserRepository;
import com.example.connect.global.common.dto.TokenDto;
import com.example.connect.global.enums.CouponUserStatus;
import com.example.connect.global.enums.UserStatus;
import com.example.connect.global.error.errorcode.ErrorCode;
import com.example.connect.global.error.exception.BadRequestException;
import com.example.connect.global.error.exception.ForbiddenException;
import com.example.connect.global.error.exception.UnAuthorizedException;
import com.example.connect.global.util.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final MembershipRepository membershipRepository;

    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final RedisEmailRepository redisEmailRepository;
    private final CouponRepository couponRepository;
    private final CouponUserRepository couponUserRepository;

    public SignupResDto signup(SignupServiceDto signupServiceDto) {

        String emailStatus = redisEmailRepository.getEmailStatus(signupServiceDto.getEmail());

        if (!"verified".equals(emailStatus)) {
            throw new BadRequestException(ErrorCode.BAD_REQUEST);
        }

        User user = signupServiceDto.toUser();

        user.updatePassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userRepository.save(user);

        redisEmailRepository.deleteEmailStatus(signupServiceDto.getEmail());
        createSignUpCoupon(savedUser);

        return new SignupResDto(savedUser);
    }

    public UserTokenResDto login(String email, String password) {

        User user = userRepository.findByEmailOrElseThrow(email);

        if (user.getStatus().equals(UserStatus.REJECTED)) {
            throw new ForbiddenException(ErrorCode.REJECTED_PERMISSION);
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new UnAuthorizedException(ErrorCode.UNAUTHORIZED_PASSWORD);
        }

        Membership membership = membershipRepository.findByUserIdAndExpiredDateAfter(user.getId(), LocalDate.now()).orElse(null);

        RedisUserDto sessionUser = membership == null ? new RedisUserDto(user) : new RedisUserDto(user, membership);

        TokenDto tokens = jwtProvider.generateToken(sessionUser);

        return new UserTokenResDto(sessionUser, tokens);
    }

    public TokenDto refresh(String refreshToken) {

        String email = jwtProvider.getUsername(refreshToken);

        if (!jwtProvider.validRefreshToken(refreshToken, email)) {
            throw new UnAuthorizedException(ErrorCode.EXPIRED_TOKEN);
        }

        User user = userRepository.findByEmailOrElseThrow(email);

        if (user.getStatus().equals(UserStatus.REJECTED)) {
            throw new ForbiddenException(ErrorCode.REJECTED_PERMISSION);
        }
        
        Membership membership = membershipRepository.findByUserIdAndExpiredDateAfter(user.getId(), LocalDate.now()).orElse(null);

        RedisUserDto sessionUser = membership == null ? new RedisUserDto(user) : new RedisUserDto(user, membership);

        return jwtProvider.generateToken(sessionUser);
    }

    public void createSignUpCoupon(User user) {
        LocalDate expiredDate = LocalDate.now().plusDays(14);
        Coupon coupon = couponRepository.findByName("가입 축하 쿠폰");

        CouponUser couponUser = new CouponUser(
                expiredDate,
                CouponUserStatus.UNUSED,
                user,
                coupon
        );

        couponUserRepository.save(couponUser);
    }
}
