package com.example.demo.dto.request;

import lombok.Data;

@Data
public class AuthenticationRequest {
    String username;
    String password;
}
