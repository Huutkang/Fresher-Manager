package com.example.demo.dto.response;



import lombok.Data;


@Data
public class NotificationResDto {
    private int id;
    private int idFresher;
    private int idProject;
    private String fresher;
    private String project;
    private String message;
}
