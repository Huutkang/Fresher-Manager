package com.example.demo.service;

import com.example.demo.entity.Project;
import com.example.demo.exception.AppException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.repository.ProjectRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.demo.dto.request.ProjectReqDto;
import com.example.demo.dto.response.ProjectResDto;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    // Thêm mới Project
    Project addProject(String name) {
        Project project = new Project();
        project.setName(name);
        return projectRepository.save(project);
    }

    public Project addProject(ProjectReqDto reqDto) {
        Project project = new Project();
        project.setName(reqDto.getName());
        project.setLanguage(reqDto.getLanguage());
        project.setStartDate(reqDto.getStartDate());
        project.setEndDate(reqDto.getEndDate());
        return projectRepository.save(project);
    }
    
    // Lấy tất cả Projects
    public List<ProjectResDto> getAllProjects() {
        return projectRepository.findAll().stream()
                .filter(Project::isActive)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Lấy Project theo ID
    public Optional<ProjectResDto> getProjectById(int id) {
        try {
            return Optional.of(convertToDTO(getProject(id)));
        } catch (RuntimeException e) {
            return Optional.empty();
        }
    }

    protected Project getProject(int id) {
        return projectRepository.findById(id)
                .filter(Project::isActive)
                .orElseThrow(() -> new AppException(ErrorCode.PROJECT_NOT_EXISTED));
    }

    // Cập nhật Project
    public Project updateProject(int id, Project projectDetails) {
        Project project = getProject(id);
        project.setName(projectDetails.getName());
        project.setCenter(projectDetails.getCenter());
        project.setManager(projectDetails.getManager());
        project.setLanguage(projectDetails.getLanguage());
        project.setStatus(projectDetails.getStatus());
        project.setStartDate(projectDetails.getStartDate());
        project.setEndDate(projectDetails.getEndDate());
        return projectRepository.save(project);
    }

    // Xóa Project theo ID
    public Project deleteProject(int id) {
        Project project = getProject(id);
        project.setActive(false);
        return projectRepository.save(project);
    }
    protected List<Project> findProjects(int centerId, int managerId){
        if (centerId > 0 && managerId > 0) {
            return projectRepository.findByCenterIdAndManagerId(centerId, managerId);
        } else if (centerId > 0) {
            return projectRepository.findByCenterId(centerId);
        } else if (managerId > 0) {
            return projectRepository.findByManagerId(managerId);
        } else {
            throw new IllegalArgumentException("At least one of userId or projectId must be provided");
        }
    }
    public ProjectResDto convertToDTO(Project project) {
        ProjectResDto dto = new ProjectResDto();
        dto.setId(project.getId());
        dto.setName(project.getName());
        dto.setIdCenter(project.getCenter().getId());
        dto.setCenter(project.getCenter().getName());
        dto.setIdManager(project.getManager().getId());
        dto.setManager(project.getManager().getName());
        dto.setLanguage(project.getLanguage());
        dto.setStatus(project.getStatus());
        dto.setStartDate(project.getStartDate());
        dto.setEndDate(project.getEndDate());
        return dto;
    }
}
