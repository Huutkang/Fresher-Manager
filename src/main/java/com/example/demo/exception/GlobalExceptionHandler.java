package com.example.demo.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.demo.dto.response.ApiResponse;


import org.springframework.security.access.AccessDeniedException;




@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AppException.class)
    ResponseEntity<ApiResponse> handleAppException(AppException e)  {
        ApiResponse response = new ApiResponse();
        response.setCode(e.getCode());
        response.setMessage(e.getMessage());
        return ResponseEntity.status(400).body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    ResponseEntity<ApiResponse> handleRuntimeException(RuntimeException e) {
        ApiResponse response = new ApiResponse();
        response.setCode(100);
        response.setMessage("RuntimeException:    "+e.getMessage());
        return ResponseEntity.status(500).body(response);
    }
    
    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<ApiResponse> handlingAccessDeniedException(AccessDeniedException e) {
        ApiResponse response = new ApiResponse();
        response.setCode(403);
        response.setMessage("bạn không có quyền");
        return ResponseEntity.status(403).body(response);
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<ApiResponse> handleException(Exception e) {
        ApiResponse response = new ApiResponse();
        response.setCode(500);
        response.setMessage("Exception:    "+e.getMessage());
        return ResponseEntity.status(500).body(response);
    }

    
}
