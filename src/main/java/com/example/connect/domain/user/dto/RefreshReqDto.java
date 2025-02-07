package com.example.connect.domain.user.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RefreshReqDto {

    private final String refreshToken;
}
