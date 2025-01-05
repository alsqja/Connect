package com.example.connect.domain.user.dto;

import com.example.connect.global.common.dto.BaseDtoType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LoginReqDto implements BaseDtoType {

    private final String email;
    private final String password;
}
