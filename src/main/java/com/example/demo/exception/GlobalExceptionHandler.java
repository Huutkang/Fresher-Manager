package com.example.demo.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.demo.dto.response.Api;
import com.example.demo.enums.Code;

import org.springframework.security.access.AccessDeniedException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AppException.class)
    ResponseEntity<Api<String>> handleAppException(AppException e) {
        return Api.response(e.getCode());
    }

    @ExceptionHandler(AccessDeniedException.class)
    ResponseEntity<Api<String>> handleAccessDeniedException(AccessDeniedException e) {
        return Api.response(Code.ACCESS_DENIED);
    }

    @ExceptionHandler(RuntimeException.class)
    ResponseEntity<Api<String>> handleRuntimeException(RuntimeException e) {
        return Api.response(Code.RUNTIME_EXCEPTION, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<Api<String>> handleException(Exception e) {
        return Api.response(Code.INTERNAL_SERVER_ERROR, e.getMessage());
    }
}
