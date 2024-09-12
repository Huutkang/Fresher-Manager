package com.example.demo.dto.response;


import lombok.Data;


@Data
public class CenterResDto {
    private int id;
    private String name;
    private String location;
    private int idManager;
    private String manager;
}
