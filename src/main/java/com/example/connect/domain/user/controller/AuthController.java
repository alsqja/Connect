package com.example.connect.domain.user.controller;

import com.example.connect.domain.user.dto.SignupServiceDto;
import com.example.connect.domain.user.dto.UserReqDto;
import com.example.connect.domain.user.dto.UserResDto;
import com.example.connect.domain.user.service.AuthService;
import com.example.connect.global.common.dto.CommonResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<CommonResDto<UserResDto>> signup(@RequestBody UserReqDto userReqDto) {

        SignupServiceDto signupServiceDto = userReqDto.toSignupServiceDto();

        UserResDto result = authService.signup(signupServiceDto);

        return new ResponseEntity<>(new CommonResDto<>("회원가입 완료", result), HttpStatus.CREATED);
    }

}
