package com.example.connect.domain.user.dto;

import com.example.connect.global.common.dto.BaseDtoType;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public class LoginReqDto implements BaseDtoType {

    private final String email;
    private final String password;

    @JsonCreator
    public LoginReqDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
