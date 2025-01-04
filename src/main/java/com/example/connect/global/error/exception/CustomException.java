package com.example.connect.global.error.exception;

import com.example.connect.global.error.errorcode.ErrorCode;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    private final ErrorCode errorCode;

    public CustomException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
