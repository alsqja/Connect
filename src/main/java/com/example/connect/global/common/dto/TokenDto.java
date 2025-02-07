package com.example.connect.global.common.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TokenDto implements BaseDtoType {

    private final String accessToken;
    private final String refreshToken;
}
