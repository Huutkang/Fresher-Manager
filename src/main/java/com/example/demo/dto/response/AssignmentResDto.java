package com.example.demo.dto.response;


import lombok.Data;


@Data
public class AssignmentResDto {
    private int id;
    private int idFresher;
    private int idProject;
    private String fresher;
    private String project;
    private int assignment_number;
    private Double score;

}
