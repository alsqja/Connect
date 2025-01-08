package com.example.connect.domain.payment.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PortoneToken {
    private String accessToken;
    private String refreshToken;
}
