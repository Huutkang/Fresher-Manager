package com.example.demo.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.demo.dto.response.ApiResponse;
import com.nimbusds.jose.JOSEException;
import java.text.ParseException;




@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AppException.class)
    ResponseEntity<ApiResponse> handleAppException(AppException e)  {
        ApiResponse response = new ApiResponse();
        response.setCode(e.getCode());
        response.setMessage(e.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    ResponseEntity<ApiResponse> handleRuntimeException(RuntimeException e) {
        ApiResponse response = new ApiResponse();
        response.setCode(100);
        response.setMessage("RuntimeException:    "+e.getMessage());
        return ResponseEntity.badRequest().body(response);
    }
   

    @ExceptionHandler(Exception.class)
    ResponseEntity<ApiResponse> handleException(Exception e) {
        ApiResponse response = new ApiResponse();
        response.setCode(500);
        response.setMessage("Exception:    "+e.getMessage());
        return ResponseEntity.badRequest().body(response);
    }
}
