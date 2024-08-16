package com.example.demo.dto.request;


import lombok.Data;

@Data
public class AuthenticationReq {
    String username;
    String password;
}