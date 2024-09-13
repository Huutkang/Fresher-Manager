package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.request.AssignmentReqDto;
import com.example.demo.dto.response.Api;
import com.example.demo.dto.response.AssignmentResDto;
import com.example.demo.enums.Code;
import com.example.demo.service.AssignmentService;

@RestController
@RequestMapping("/assignments")
public class AssignmentController {

    @Autowired
    private AssignmentService assignmentService;

    // Thêm mới Assignment
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @PostMapping
    public ResponseEntity<Api<AssignmentResDto>> createAssignment(@RequestBody AssignmentReqDto assignment) {
        AssignmentResDto newAssignment = assignmentService.addAssignment(assignment);
        return Api.response(Code.OK, newAssignment);
    }

    // Lấy tất cả Assignments
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @GetMapping
    public ResponseEntity<Api<List<AssignmentResDto>>> getAllAssignments() {
        List<AssignmentResDto> assignments = assignmentService.getAllAssignments();
        return Api.response(Code.OK, assignments);
    }

    // Lấy Assignment theo ID
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Api<AssignmentResDto>> getAssignmentById(@PathVariable int id) {
        Optional<AssignmentResDto> assignment = assignmentService.getAssignmentById(id);
        if (assignment.isEmpty()) {
            return Api.response(Code.ASSIGNMENT_NOT_EXISTED);
        }
        return Api.response(Code.OK, assignment.get());
    }

    // Cập nhật Assignment
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Api<AssignmentResDto>> updateAssignment(@PathVariable int id, @RequestBody AssignmentReqDto assignmentDetails) {
        AssignmentResDto updatedAssignment = assignmentService.updateAssignment(id, assignmentDetails);
        return Api.response(Code.OK, updatedAssignment);

    }

    // Xóa Assignment theo ID
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Api<Void>> deleteAssignment(@PathVariable int id) {
        assignmentService.deleteAssignment(id);
        return Api.response(Code.OK);
    }
}
