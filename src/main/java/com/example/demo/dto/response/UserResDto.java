package com.example.demo.dto.response;

import java.util.Set;

import lombok.Data;


@Data
public class UserResDto {
    private int id;
    private String username;
    private String name;
    private String email;
    private String phoneNumber;
    private Set<String> roles;
}
