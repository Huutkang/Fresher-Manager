package com.example.demo.dto.request;

import lombok.Data;


@Data
public class AssignmentReqDto {
    private int IdFresher;
    private int IdProject;
    private int assignment_number;
    private Double score;

}
