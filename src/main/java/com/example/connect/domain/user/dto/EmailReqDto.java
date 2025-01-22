package com.example.connect.domain.user.dto;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class EmailReqDto {

    @Email(message = "잘못된 이메일 입니다.")
    private final String email;
}
