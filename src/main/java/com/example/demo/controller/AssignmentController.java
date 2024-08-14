package com.example.demo.controller;

import com.example.demo.entity.Assignment;
import com.example.demo.service.AssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/assignments")
public class AssignmentController {

    @Autowired
    private AssignmentService assignmentService;

    // Thêm mới Assignment
    @PostMapping
    public Assignment createAssignment(@RequestBody Assignment assignment) {
        return assignmentService.addAssignment(assignment);
    }

    // Lấy tất cả Assignments
    @GetMapping
    public List<Assignment> getAllAssignments() {
        return assignmentService.getAllAssignments();
    }

    // Lấy Assignment theo ID
    @GetMapping("/{id}")
    public ResponseEntity<Assignment> getAssignmentById(@PathVariable int id) {
        Assignment assignment = assignmentService.getAssignmentById(id).orElse(null);
        if (assignment == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(assignment);
    }

    // Cập nhật Assignment
    @PutMapping("/{id}")
    public ResponseEntity<Assignment> updateAssignment(@PathVariable int id, @RequestBody Assignment assignmentDetails) {
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
