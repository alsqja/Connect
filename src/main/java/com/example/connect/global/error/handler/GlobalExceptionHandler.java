package com.example.connect.global.error.handler;

import com.example.connect.global.error.exception.BadRequestException;
import com.example.connect.global.error.exception.CustomException;
import com.example.connect.global.error.exception.ForbiddenException;
import com.example.connect.global.error.exception.NotFoundException;
import com.example.connect.global.error.exception.UnAuthorizedException;
import com.example.connect.global.error.response.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({CustomException.class, BadRequestException.class, ForbiddenException.class, NotFoundException.class, UnAuthorizedException.class})
    public ResponseEntity<ErrorResponse> handleBadRequestException(CustomException e) {

        return ErrorResponse.toResponseEntity(e.getErrorCode());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {

        return ErrorResponse.toResponseEntity(e);
    }
}
