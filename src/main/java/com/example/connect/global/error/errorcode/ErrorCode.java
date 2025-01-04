package com.example.connect.global.error.errorcode;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // 400 BAD_REQUEST
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청 입니다."),
    INVALID_FILE(HttpStatus.BAD_REQUEST, "지원하지 않는 양식의 파일입니다."),
    LARGE_FILE(HttpStatus.BAD_REQUEST, "파일의 용량은 최대 5MB 입니다."),
    BAD_INPUT(HttpStatus.BAD_REQUEST, "잘못된 입력값 입니다."),

    // 401 UNAUTHORIZED
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."),
    UNAUTHORIZED_PASSWORD(HttpStatus.UNAUTHORIZED, "잘못된 비밀번호 입니다."),
    UNCHECKED_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호 확인이 필요합니다."),
    UNAUTHORIZED_USER(HttpStatus.UNAUTHORIZED, "본인이 아닙니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "잘못된 토큰 입니다."),

    // 403 FORBIDDEN
    FORBIDDEN_PERMISSION(HttpStatus.FORBIDDEN, "권한이 없습니다."),

    // 404 NOT_FOUND
    NOT_FOUND(HttpStatus.NOT_FOUND, "없는 데이터 입니다.");

    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
