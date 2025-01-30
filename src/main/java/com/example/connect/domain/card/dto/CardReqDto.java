package com.example.connect.domain.card.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CardReqDto {
    private final String number;
    private final String expiryYear;
    private final String expiryMonth;
    private final String passwordTwoDigits;
    private final String birthOrBusinessRegistrationNumber;
}
