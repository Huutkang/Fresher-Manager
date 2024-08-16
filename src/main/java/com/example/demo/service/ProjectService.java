package com.example.demo.service;

import com.example.demo.entity.Project;
import com.example.demo.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    // Thêm mới Project
    public Project addProject(Project project) {
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
