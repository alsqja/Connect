package com.example.connect.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public class PasswordReqDto {

    private final String password;

    @JsonCreator
    public PasswordReqDto(String password) {
        this.password = password;
    }
}
