package com.example.connect.domain.user.controller;

import com.example.connect.domain.user.dto.EmailReqDto;
import com.example.connect.domain.user.dto.LoginReqDto;
import com.example.connect.domain.user.dto.RefreshReqDto;
import com.example.connect.domain.user.dto.SignupResDto;
import com.example.connect.domain.user.dto.SignupServiceDto;
import com.example.connect.domain.user.dto.UserReqDto;
import com.example.connect.domain.user.dto.UserTokenResDto;
import com.example.connect.domain.user.repository.RedisTokenRepository;
import com.example.connect.domain.user.service.AuthService;
import com.example.connect.domain.user.service.EmailService;
import com.example.connect.domain.user.service.NaverAuthService;
import com.example.connect.global.common.dto.CommonResDto;
import com.example.connect.global.common.dto.TokenDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final RedisTokenRepository redisTokenRepository;
    private final NaverAuthService naverAuthService;
    private final EmailService emailService;

    @PostMapping("/signup")
    public ResponseEntity<CommonResDto<SignupResDto>> signup(@Valid @RequestBody UserReqDto userReqDto) {

        SignupServiceDto signupServiceDto = userReqDto.toSignupServiceDto();

        SignupResDto result = authService.signup(signupServiceDto);

        return new ResponseEntity<>(new CommonResDto<>("회원가입 완료", result), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<CommonResDto<UserTokenResDto>> login(
            @Valid @RequestBody LoginReqDto loginReqDto
    ) {

        UserTokenResDto result = authService.login(loginReqDto.getEmail(), loginReqDto.getPassword());

        return new ResponseEntity<>(new CommonResDto<>("로그인 완료", result), HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {

        if (authentication != null && authentication.isAuthenticated()) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
            redisTokenRepository.deleteUser(authentication.getName());
            redisTokenRepository.deleteRefreshToken(authentication.getName());
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/refresh")
    public ResponseEntity<CommonResDto<TokenDto>> refreshAccessToken(
            @Valid @RequestBody RefreshReqDto dto
    ) {

        TokenDto result = authService.refresh(dto.getRefreshToken());

        return new ResponseEntity<>(new CommonResDto<>("토큰 재발급 완료", result), HttpStatus.CREATED);
    }

    @GetMapping("/login/naver/code")
    public ResponseEntity<CommonResDto<UserTokenResDto>> loginCallback(
            @RequestParam String code,
            @RequestParam String state
    ) {

        UserTokenResDto result = naverAuthService.handleNaverLogin(code, state);

        return new ResponseEntity<>(new CommonResDto<>("로그인 완료", result), HttpStatus.OK);
    }

    @PostMapping("/email")
    public ResponseEntity<Void> mailSend(@Valid @RequestBody EmailReqDto emailReqDto) {

        emailService.sendEmail(emailReqDto.getEmail());

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/email/verify")
    public ResponseEntity<Void> verifyEmail(@Valid @RequestBody EmailReqDto emailReqDto) {

        emailService.verifyCode(emailReqDto.getEmail(), emailReqDto.getCode());

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
