package com.example.connect.global.error.exception;

import com.example.connect.global.error.errorcode.ErrorCode;
import lombok.Getter;

@Getter
public class UnAuthorizedException extends CustomException {

    public UnAuthorizedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
