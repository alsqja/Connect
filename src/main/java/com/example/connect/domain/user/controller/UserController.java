package com.example.connect.domain.user.controller;

import com.example.connect.domain.user.dto.RedisUserDto;
import com.example.connect.domain.user.dto.UpdateUserReqDto;
import com.example.connect.domain.user.dto.UpdateUserServiceDto;
import com.example.connect.domain.user.dto.UserResDto;
import com.example.connect.domain.user.service.UserService;
import com.example.connect.global.common.dto.CommonResDto;
import com.example.connect.global.config.auth.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users/my")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<CommonResDto<UserResDto>> findMe(Authentication authentication) {

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        RedisUserDto me = userDetails.getUser();

        return new ResponseEntity<>(new CommonResDto<>("프로필 조회 완료", new UserResDto(me)), HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity<CommonResDto<UserResDto>> updateMe(
            @RequestBody UpdateUserReqDto dto,
            Authentication authentication
    ) {

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        RedisUserDto me = userDetails.getUser();

        UpdateUserServiceDto serviceDto = dto.toServiceDto(me);

        UserResDto result = userService.updateMe(serviceDto);

        return new ResponseEntity<>(new CommonResDto<>("프로필 수정 완료", result), HttpStatus.OK);
    }
}
