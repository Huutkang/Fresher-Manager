package com.example.demo.dto.request;

import lombok.Data;


@Data
public class AssignmentReqDto {
    private int idFresher;
    private int idProject;
    private int assignmentNumber;
    private Double score;

}
