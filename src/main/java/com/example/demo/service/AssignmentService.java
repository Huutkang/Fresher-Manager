package com.example.demo.service;

import com.example.demo.entity.Assignment;
import com.example.demo.repository.AssignmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AssignmentService {

    @Autowired
    private AssignmentRepository assignmentRepository;

    // Thêm mới Assignment
    public Assignment addAssignment(Assignment assignment) {
        return assignmentRepository.save(assignment);
    }

    // Lấy tất cả Assignments
    public List<Assignment> getAllAssignments() {
        return assignmentRepository.findAll();
    }

    // Lấy Assignment theo ID
    public Optional<Assignment> getAssignmentById(int id) {
        return assignmentRepository.findById(id);
    }

    // Cập nhật Assignment
    public Assignment updateAssignment(int id, Assignment assignmentDetails) {
        Assignment assignment = assignmentRepository.findById(id).orElseThrow(() -> new RuntimeException("Assignment not found"));
        assignment.setFresher(assignmentDetails.getFresher());
        assignment.setProject(assignmentDetails.getProject());
        assignment.setAssignmentNumber(assignmentDetails.getAssignmentNumber());
        assignment.setScore(assignmentDetails.getScore());
        return assignmentRepository.save(assignment);
    }

    // Xóa Assignment theo ID
    public void deleteAssignment(int id) {
        assignmentRepository.deleteById(id);
    }
}
