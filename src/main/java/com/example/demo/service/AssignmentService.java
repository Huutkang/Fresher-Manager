package com.example.demo.service;

import com.example.demo.entity.Assignment;
import com.example.demo.enums.Code;
import com.example.demo.exception.AppException;
import com.example.demo.repository.AssignmentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.demo.dto.request.AssignmentReqDto;
import com.example.demo.dto.response.AssignmentResDto;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Service
public class AssignmentService {

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private FresherService fresherService;

    private static final Logger log = LogManager.getLogger(AssignmentService.class);
    
    // Thêm mới Assignment
    public Assignment addAssignment(int fresher_id, int project_id, int assignmentNumber, Double score) {
        Assignment assignment = new Assignment();
        assignment.setFresher(fresherService.getFresher(fresher_id));
        assignment.setProject(projectService.getProject(project_id));
        assignment.setAssignmentNumber(assignmentNumber);
        assignment.setScore(score);
        return assignmentRepository.save(assignment);
    }

    public AssignmentResDto addAssignment(AssignmentReqDto req) {
        System.out.println(req);
        Assignment assignment = new Assignment();
        assignment.setFresher(fresherService.getFresher(req.getIdFresher()));
        assignment.setProject(projectService.getProject(req.getIdProject()));
        assignment.setAssignmentNumber(req.getAssignmentNumber());
        assignment.setScore(req.getScore());
        return convertToDTO(assignmentRepository.save(assignment));
    }

    // Lấy tất cả Assignments
    public List<AssignmentResDto> getAllAssignments() {
        return assignmentRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    protected Assignment getAssignment(int id) {
        return assignmentRepository.findById(id)
        .orElseThrow(() -> new AppException(Code.ASSIGNMENT_NOT_EXISTED));
    }

    public Optional<AssignmentResDto> getAssignmentById(int id) {
        try {
            return Optional.of(convertToDTO(getAssignment(id)));
        } catch (RuntimeException e) {
            return Optional.empty();
        }
    }

    // Cập nhật Assignment
    public AssignmentResDto updateAssignment(int id, AssignmentReqDto req) {
        Assignment assignment = getAssignment(id);
        if (req.getIdFresher() != null) {
            assignment.setFresher(fresherService.getFresher(req.getIdFresher()));
        }
        if (req.getIdProject() != null) {
            assignment.setProject(projectService.getProject(req.getIdProject()));
        }
        if (req.getAssignmentNumber() != null) {
            assignment.setAssignmentNumber(req.getAssignmentNumber());
        }
        if (req.getScore() != null) {
            assignment.setScore(req.getScore());
        }
            return convertToDTO(assignmentRepository.save(assignment));
    }
    

    // Xóa Assignment theo ID
    public void deleteAssignment(int id) {
        assignmentRepository.deleteById(id);
    }

    // Tìm kiếm Assignments theo fresherId và projectId và trả về DTO
    public List<AssignmentResDto> findAssignments(Integer fresherId, Integer projectId) {
        List<Assignment> assignments;
        if (fresherId != null && projectId != null) {
            assignments = assignmentRepository.findByFresherIdAndProjectId(fresherId, projectId);
        } else if (fresherId != null) {
            assignments = assignmentRepository.findByFresherId(fresherId);
        } else if (projectId != null) {
            assignments = assignmentRepository.findByProjectId(projectId);
        } else {
            assignments = assignmentRepository.findAll();
        }
        return assignments.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Tìm kiếm Assignment theo assignment number và trả về DTO
    public List<AssignmentResDto> findByAssignmentNumber(Integer assignmentNumber) {
        return assignmentRepository.findByAssignmentNumber(assignmentNumber).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Tìm kiếm Assignment theo điểm số và trả về DTO
    public List<AssignmentResDto> findByScore(Double score) {
        return assignmentRepository.findByScore(score).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Tìm kiếm Assignment theo assignment number và điểm số, trả về DTO
    public List<AssignmentResDto> findByAssignmentNumberAndScore(Integer assignmentNumber, Double score) {
        return assignmentRepository.findByAssignmentNumberAndScore(assignmentNumber, score).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Chuyển đổi từ entity sang DTO
    protected AssignmentResDto convertToDTO(Assignment assignment) {
        AssignmentResDto res = new AssignmentResDto();
        res.setId(assignment.getId());
        res.setIdFresher(assignment.getFresher().getId());
        res.setIdProject(assignment.getProject().getId());
        res.setFresher(assignment.getFresher().getUser().getName());
        res.setProject(assignment.getProject().getName());
        res.setAssignmentNumber(assignment.getAssignmentNumber());
        res.setScore(assignment.getScore());
        return res;
    }
}
