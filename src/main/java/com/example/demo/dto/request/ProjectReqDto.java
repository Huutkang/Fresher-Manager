package com.example.demo.dto.request;

import java.sql.Date;

import lombok.Data;

@Data
public class ProjectReqDto {
    private String name;
    private String language;
    private Date startDate;
    private Date endDate;

}
