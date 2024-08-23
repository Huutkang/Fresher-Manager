package com.example.demo.service;

import com.example.demo.entity.FresherProject;
import com.example.demo.exception.AppException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.repository.FresherProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FresherProjectService {

    @Autowired
    private FresherProjectRepository fresherProjectRepository;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private FresherService fresherService;
    
    // Thêm mới FresherProject
    public FresherProject addFresherProject(int fresher_id, int project_id, String role) {
        FresherProject fresherProject = new FresherProject();
        fresherProject.setFresher(fresherService.getFresherById(fresher_id).orElseThrow(() -> new AppException(ErrorCode.FRESHER_NOT_EXISTED)));
        fresherProject.setProject(projectService.getProjectById(project_id).orElseThrow(() -> new AppException(ErrorCode.PROJECT_NOT_EXISTED)));
        fresherProject.setRole(role);
        return fresherProjectRepository.save(fresherProject);
    }

    // Lấy tất cả FresherProjects
    public List<FresherProject> getAllFresherProjects() {
        return fresherProjectRepository.findAll();
    }

    // Lấy FresherProject theo ID
    public Optional<FresherProject> getFresherProjectById(int id) {
        return fresherProjectRepository.findById(id);
    }

    // Cập nhật FresherProject
    public FresherProject updateFresherProject(int id, FresherProject fresherProjectDetails) {
        FresherProject fresherProject = fresherProjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("FresherProject not found"));
        fresherProject.setUser(fresherProjectDetails.getUser());
        fresherProject.setProject(fresherProjectDetails.getProject());
        fresherProject.setRole(fresherProjectDetails.getRole());
        return fresherProjectRepository.save(fresherProject);
    }

    // Xóa FresherProject theo ID
    public void deleteFresherProject(int id) {
        fresherProjectRepository.deleteById(id);
    }
}
