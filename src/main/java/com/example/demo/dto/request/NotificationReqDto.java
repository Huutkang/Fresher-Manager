package com.example.demo.dto.request;

import lombok.Data;

@Data
public class NotificationReqDto {
    private int idFresher;
    private int idProject;
    private String message;
}
