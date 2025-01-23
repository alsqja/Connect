package com.example.connect.domain.user.service;

import com.example.connect.domain.membership.entity.Membership;
import com.example.connect.domain.membership.repository.MembershipRepository;
import com.example.connect.domain.user.dto.RedisUserDto;
import com.example.connect.domain.user.dto.SignupResDto;
import com.example.connect.domain.user.dto.SignupServiceDto;
import com.example.connect.domain.user.dto.UserTokenResDto;
import com.example.connect.domain.user.entity.User;
import com.example.connect.domain.user.repository.RedisEmailRepository;
import com.example.connect.domain.user.repository.RedisTokenRepository;
import com.example.connect.domain.user.repository.UserRepository;
import com.example.connect.global.common.dto.TokenDto;
import com.example.connect.global.error.errorcode.ErrorCode;
import com.example.connect.global.error.exception.BadRequestException;
import com.example.connect.global.error.exception.UnAuthorizedException;
import com.example.connect.global.util.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final MembershipRepository membershipRepository;

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final RedisTokenRepository redisTokenRepository;
    private final RedisEmailRepository redisEmailRepository;

    public SignupResDto signup(SignupServiceDto signupServiceDto) {

        String emailStatus = redisEmailRepository.getEmailStatus(signupServiceDto.getEmail());

        if (!"verified".equals(emailStatus)) {
            throw new BadRequestException(ErrorCode.BAD_REQUEST);
        }

        boolean isExist = userRepository.existsByEmail(signupServiceDto.getEmail()) > 0;

        if (isExist) {
            throw new BadRequestException(ErrorCode.INVALID_EMAIL);
        }

        User user = signupServiceDto.toUser();

        user.updatePassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userRepository.save(user);

        redisEmailRepository.deleteEmailStatus(signupServiceDto.getEmail());

        return new SignupResDto(savedUser);
    }

    public UserTokenResDto login(String email, String password) {

        User user = userRepository.findByEmailOrElseThrow(email);

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new UnAuthorizedException(ErrorCode.UNAUTHORIZED_PASSWORD);
        }

        Membership membership = membershipRepository.findByUserIdAndExpiredDateAfter(user.getId(), LocalDate.now()).orElse(null);

        RedisUserDto sessionUser;

        if (membership == null) {
            sessionUser = new RedisUserDto(user);
        } else {
            sessionUser = new RedisUserDto(user, membership);
        }

        TokenDto tokens = jwtProvider.generateToken(sessionUser);

        return new UserTokenResDto(sessionUser, tokens);
    }

    public TokenDto refresh(String refreshToken) {

        String email = jwtProvider.getUsername(refreshToken);

        User user = userRepository.findByEmailOrElseThrow(email);
        Membership membership = membershipRepository.findByUserIdAndExpiredDateAfter(user.getId(), LocalDate.now()).orElse(null);

        RedisUserDto sessionUser;

        if (membership == null) {
            sessionUser = new RedisUserDto(user);
        } else {
            sessionUser = new RedisUserDto(user, membership);
        }

        if (!jwtProvider.validRefreshToken(refreshToken, email)) {
            throw new UnAuthorizedException(ErrorCode.EXPIRED_TOKEN);
        }

        return jwtProvider.generateToken(sessionUser);
    }
}
