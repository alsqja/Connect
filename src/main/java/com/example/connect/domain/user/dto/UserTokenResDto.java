package com.example.connect.domain.user.dto;

import com.example.connect.global.common.dto.BaseDtoType;
import com.example.connect.global.common.dto.TokenDto;
import com.example.connect.global.enums.MembershipType;
import com.example.connect.global.enums.UserRole;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public class UserTokenResDto implements BaseDtoType {

    private final Long id;
    private final String email;
    private final String name;
    private final String profileUrl;
    private final UserRole role;
    private final MembershipType memberType;
    private final LocalDate expiredDate;
    private final String accessToken;
    private final String refreshToken;

    public UserTokenResDto(RedisUserDto user, TokenDto tokens) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.name = user.getName();
        this.profileUrl = user.getProfileUrl();
        this.role = user.getRole();
        this.memberType = user.getMembershipType();
        this.expiredDate = user.getExpiredDate();
        this.accessToken = tokens.getAccessToken();
        this.refreshToken = tokens.getRefreshToken();
    }
}
