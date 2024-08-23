package com.example.demo.service;

import com.example.demo.entity.Project;
import com.example.demo.repository.ProjectRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import com.example.demo.dto.request.ProjectReqDto;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    // Thêm mới Project
    public Project addProject(String name) {
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
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    // Lấy Project theo ID
    public Optional<Project> getProjectById(int id) {
        return projectRepository.findById(id);
    }

    // Cập nhật Project
    public Project updateProject(int id, Project projectDetails) {
        Project project = projectRepository.findById(id).orElseThrow(() -> new RuntimeException("Project not found"));
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
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        project.setActive(false);
        return projectRepository.save(project);
    }
}
