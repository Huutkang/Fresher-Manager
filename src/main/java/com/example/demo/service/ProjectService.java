package com.example.demo.service;

import com.example.demo.entity.Project;
import com.example.demo.enums.Code;
import com.example.demo.exception.AppException;
import com.example.demo.repository.ProjectRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.demo.dto.request.ProjectReqDto;
import com.example.demo.dto.response.ProjectResDto;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    private static final Logger log = LogManager.getLogger(ProjectService.class);
    
    // Thêm mới Project
    Project addProject(String name) {
        Project project = new Project();
        project.setName(name);
        log.info("Add Project " + project.getName());
        return projectRepository.save(project);
    }

    public ProjectResDto addProject(ProjectReqDto reqDto) {
        Project project = new Project();
        project.setName(reqDto.getName());
        project.setLanguage(reqDto.getLanguage());
        project.setStartDate(reqDto.getStartDate());
        project.setEndDate(reqDto.getEndDate());
        log.info("Add Project " + project.getName());
        return convertToDTO(projectRepository.save(project));
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
                .orElseThrow(() -> new AppException(Code.PROJECT_NOT_EXISTED));
    }

    // Cập nhật Project
    public ProjectResDto updateProject(int id, ProjectReqDto reqDto) {
        Project project = getProject(id);
        project.setName(reqDto.getName());
        project.setLanguage(reqDto.getLanguage());
        project.setStartDate(reqDto.getStartDate());
        project.setEndDate(reqDto.getEndDate());
        log.info("Update Project " + project.getName());
        return convertToDTO(projectRepository.save(project));
    }

    // Xóa Project theo ID
    public Project deleteProject(int id) {
        Project project = getProject(id);
        project.setActive(false);
        log.info("Delete Project " + project.getName());
        return projectRepository.save(project);
    }

    public List<ProjectResDto> findProjects(int centerId, int managerId) {
        List<Project> projects;
        if (centerId > 0 && managerId > 0) {
            projects = projectRepository.findByCenterIdAndManagerId(centerId, managerId);
        } else if (centerId > 0) {
            projects = projectRepository.findByCenterId(centerId);
        } else if (managerId > 0) {
            projects = projectRepository.findByManagerId(managerId);
        } else {
            throw new IllegalArgumentException("Phải cung cấp ít nhất một trong số centerId hoặc managerId");
        }
        return projects.stream()
                       .filter(Project::isActive)
                       .map(this::convertToDTO)
                       .collect(Collectors.toList());
    }
    
    public List<ProjectResDto> findByName(String name) {
        return projectRepository.findByName(name)
              .stream()
              .filter(Project::isActive)
              .map(this::convertToDTO)
              .collect(Collectors.toList());
    }
    
    public List<ProjectResDto> findByLanguage(String language) {
        return projectRepository.findByLanguage(language)
              .stream()
              .filter(Project::isActive)
              .map(this::convertToDTO)
              .collect(Collectors.toList());
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
