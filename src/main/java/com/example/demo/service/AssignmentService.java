package com.example.demo.service;

import com.example.demo.entity.Assignment;
import com.example.demo.exception.AppException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.repository.AssignmentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.demo.dto.request.AssignmentReqDto;
import com.example.demo.dto.response.AssignmentResDto;

@Service
public class AssignmentService {

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private FresherService fresherService;

    // Thêm mới Assignment
    public Assignment addAssignment(int fresher_id, int project_id, int assignment_number, Double score) {
        Assignment assignment = new Assignment();
        assignment.setFresher(fresherService.getFresher(fresher_id));
        assignment.setProject(projectService.getProject(project_id));
        assignment.setAssignmentNumber(assignment_number);
        assignment.setScore(score);
        return assignmentRepository.save(assignment);
    }

    public Assignment addAssignment(AssignmentReqDto req) {
        Assignment assignment = new Assignment();
        assignment.setFresher(fresherService.getFresher(req.getIdFresher()));
        assignment.setProject(projectService.getProject(req.getProject()));
        assignment.setAssignmentNumber(req.getAssignment_number());
        assignment.setScore(req.getScore());
        return assignmentRepository.save(assignment);
    }

    // Lấy tất cả Assignments
    public List<AssignmentResDto> getAllAssignments() {
        return assignmentRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    protected Assignment getAssignment(int id) {
        return assignmentRepository.findById(id)
        .orElseThrow(() -> new AppException(ErrorCode.ASSIGNMENT_NOT_EXISTED));
    }

    public Optional<AssignmentResDto> getAssignmentById(int id) {
        try {
            return Optional.of(convertToDTO(getAssignment(id)));
        } catch (RuntimeException e) {
            return Optional.empty();
        }
    }

    // Cập nhật Assignment
    public Assignment updateAssignment(int id, AssignmentReqDto req) {
        Assignment assignment = getAssignment(id);
        assignment.setFresher(fresherService.getFresher(req.getIdFresher()));
        assignment.setProject(projectService.getProject(req.getProject()));
        assignment.setAssignmentNumber(req.getAssignment_number());
        assignment.setScore(req.getScore());
        return assignmentRepository.save(assignment);
    }

    // Xóa Assignment theo ID
    public void deleteAssignment(int id) {
        assignmentRepository.deleteById(id);
    }

    protected AssignmentResDto convertToDTO(Assignment assignment) {    
        AssignmentResDto res = new AssignmentResDto();
        res.setId(assignment.getId());
        res.setIdFresher(assignment.getFresher().getId());
        res.setIdProject(assignment.getProject().getId());
        res.setFresher(assignment.getFresher().getName());
        res.setProject(assignment.getProject().getName());
        res.setAssignmentNumber(assignment.getAssignmentNumber());
        res.setScore(assignment.getScore());
        return res;
    }

    protected List<Assignment> findAssignments(Integer fresherId, Integer projectId) {
        if (fresherId != null && projectId != null) {
            return assignmentRepository.findByFresherIdAndProjectId(fresherId, projectId);
        } else if (fresherId != null) {
            return assignmentRepository.findByFresherId(fresherId);
        } else if (projectId != null) {
            return assignmentRepository.findByProjectId(projectId);
        } else {
            return assignmentRepository.findAll();
        }
    }
}
