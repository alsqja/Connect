package com.example.connect.global.error.exception;

import com.example.connect.global.error.errorcode.ErrorCode;
import lombok.Getter;

@Getter
public class ForbiddenException extends CustomException {

    public ForbiddenException(ErrorCode errorCode) {
        super(errorCode);
    }
}
