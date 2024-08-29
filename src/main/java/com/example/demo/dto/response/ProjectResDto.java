package com.example.demo.dto.response;

import java.sql.Date;


import lombok.Data;




@Data
public class ProjectResDto {
    private int id;
    private String name;
    private int idCenter;
    private String center;
    private int idManager;
    private String manager;
    private String language;
    private String status;
    private Date startDate;
    private Date endDate;
}
