package com.example.connect.domain.user.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UpdateUserServiceDto {

    private final RedisUserDto sessionUser;
    private final String name;
    private final String oldPassword;
    private final String newPassword;
    private final String profileUrl;
    private final Boolean isActiveMatching;
}
