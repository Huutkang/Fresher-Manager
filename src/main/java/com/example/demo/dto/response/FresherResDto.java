package com.example.demo.dto.response;

import com.example.demo.entity.Center;

import lombok.Data;



@Data
public class FresherResDto {
    private int id;
    private int idUser;
    private String username;
    private String name;
    private String email;
    private String phoneNumber;
    private String programmingLanguage;
    private Center center;
}
