package com.example.demo.dto.response;



import org.springframework.http.ResponseEntity;

import com.example.demo.enums.Code;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;




@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Api<T> {
    private int code;
    private String message;
    private T result;

    public static <T> ResponseEntity<Api<T>> response(Code code) {
        Api<T> response = new Api<>(code.getCode(), code.getMessage(), null);
        return ResponseEntity.status(code.getStatusCode()).body(response);
    }

    public static <T> ResponseEntity<Api<T>> response(Code code, T result) {
        Api<T> response = new Api<>(code.getCode(), code.getMessage(), result);
        return ResponseEntity.status(code.getStatusCode()).body(response);
    }

}
