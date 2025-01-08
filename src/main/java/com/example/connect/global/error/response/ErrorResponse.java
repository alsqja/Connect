package com.example.connect.global.error.response;

import com.example.connect.global.error.errorcode.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
public class ErrorResponse {
    private final LocalDateTime timestamp = LocalDateTime.now();
    private final int status;
    private final String error;
    private final String code;
    private final String message;

    public ErrorResponse(int status, String error, String code, String message) {
        this.status = status;
        this.error = error;
        this.code = code;
        this.message = message;
    }

    public static ResponseEntity<ErrorResponse> toResponseEntity(ErrorCode errorCode) {

        return new ResponseEntity<>(new ErrorResponse(errorCode.getHttpStatus().value(), errorCode.getHttpStatus().name(), errorCode.name(), errorCode.getMessage()), errorCode.getHttpStatus());
    }

    public static ResponseEntity<ErrorResponse> toResponseEntity(MethodArgumentNotValidException e) {

        HttpStatus status = HttpStatus.BAD_REQUEST;

        return new ResponseEntity<>(
                new ErrorResponse(
                        status.value(),
                        status.name(),
                        "BAD_INPUT",
                        Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage()
                ),
                status
        );
    }
}
