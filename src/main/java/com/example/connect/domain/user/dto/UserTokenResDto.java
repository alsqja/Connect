package com.example.connect.domain.user.dto;

import com.example.connect.global.common.dto.BaseDtoType;
import com.example.connect.global.enums.MembershipType;
import com.example.connect.global.enums.UserRole;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public class UserTokenResDto implements BaseDtoType {

    private final Long id;
    private final UserRole role;
    private final MembershipType memberType;
    private final LocalDate expiredDate;
    private final String accessToken;
    private final String refreshToken;
}
