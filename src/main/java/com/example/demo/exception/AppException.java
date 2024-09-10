package com.example.demo.exception;

import com.example.demo.enums.Code;

import lombok.Getter;


@Getter
public class AppException extends RuntimeException {
    private Code code;
    public AppException(Code err) {
        this.code = err;
    }
}

