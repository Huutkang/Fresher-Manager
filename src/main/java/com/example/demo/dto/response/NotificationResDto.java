package com.example.demo.dto.response;



import lombok.Data;


@Data
public class NotificationResDto {
    private int id;
    private int idUser;
    private int idProject;
    private String user;
    private String project;
    private String message;
}
