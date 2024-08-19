package com.example.demo.exception;

import org.springframework.http.HttpStatusCode;

import lombok.Getter;



@Getter
public class AppException extends RuntimeException {
    private int code;
    private String message;
    private HttpStatusCode statusCode;

    public AppException(ErrorCode err) {
        this.code = err.getCode();
        this.message = err.getMessage();
        this.statusCode = err.getStatusCode();
    }
    
    public AppException(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}

