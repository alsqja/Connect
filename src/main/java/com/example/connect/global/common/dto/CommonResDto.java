package com.example.connect.global.common.dto;

import lombok.Getter;

@Getter
public class CommonResDto<T extends BaseDtoType> {
    
    private final String message;

    private final T data;

    public CommonResDto(String message, T data) {
        this.message = message;
        this.data = data;
    }
}
