package com.example.demo.exception;

import lombok.Getter;



@Getter
public class AppException extends RuntimeException {
    private ErrorCode err;

    public AppException(ErrorCode err) {
        super(err.getMessage());
        this.err = err;
    }
    
}

