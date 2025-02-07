package com.example.connect.global.error.exception;

import com.example.connect.global.error.errorcode.ErrorCode;
import lombok.Getter;

@Getter
public class BadRequestException extends CustomException {

    public BadRequestException(ErrorCode errorCode) {
        super(errorCode);
    }
}
