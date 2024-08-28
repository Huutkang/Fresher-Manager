package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.request.AssignmentReqDto;
import com.example.demo.dto.response.AssignmentResDto;
import com.example.demo.entity.Assignment;
import com.example.demo.service.AssignmentService;

@RestController
@RequestMapping("/assignments")
public class AssignmentController {

    @Autowired
    private AssignmentService assignmentService;

    // Thêm mới Assignment
    @PostMapping
    public Assignment createAssignment(@RequestBody AssignmentReqDto assignment) {
        return assignmentService.addAssignment(assignment);
    }

    // Lấy tất cả Assignments
    @GetMapping
    public List<AssignmentResDto> getAllAssignments() {
        return assignmentService.getAllAssignments();
    }

    // Lấy Assignment theo ID
    @GetMapping("/{id}")
    public ResponseEntity<AssignmentResDto> getAssignmentById(@PathVariable int id) {
        AssignmentResDto assignment = assignmentService.getAssignmentById(id).orElse(null);
        if (assignment == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(assignment);
    }

    // Cập nhật Assignment
    @PutMapping("/{id}")
    public ResponseEntity<Assignment> updateAssignment(@PathVariable int id, @RequestBody AssignmentReqDto assignmentDetails) {
        try {
            Assignment updatedAssignment = assignmentService.updateAssignment(id, assignmentDetails);
            return ResponseEntity.ok(updatedAssignment);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Xóa Assignment theo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAssignment(@PathVariable int id) {
        assignmentService.deleteAssignment(id);
        return ResponseEntity.noContent().build();
    }
}
