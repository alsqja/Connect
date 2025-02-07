package com.example.connect.domain.payment.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class EncryptReqDto {
    private final String encryptData;
}
