package com.example.demo.dto.request;

import lombok.Data;


@Data
public class SetUserReqDto {
    private String username;
    private String name;
    private String email;
    private String phoneNumber;
}

