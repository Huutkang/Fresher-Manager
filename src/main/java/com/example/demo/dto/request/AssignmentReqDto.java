package com.example.demo.dto.request;

import lombok.Data;


@Data
public class AssignmentReqDto {
    private Integer idFresher;
    private Integer idProject;
    private Integer assignmentNumber;
    private Double score;

}
