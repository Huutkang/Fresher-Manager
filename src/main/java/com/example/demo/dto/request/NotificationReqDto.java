package com.example.demo.dto.request;

import lombok.Data;

@Data
public class NotificationReqDto {
    private Integer idFresher;
    private Integer idProject;
    private String message;
}
