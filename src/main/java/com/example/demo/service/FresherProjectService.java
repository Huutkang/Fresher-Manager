package com.example.demo.service;

import com.example.demo.entity.FresherProject;
import com.example.demo.repository.FresherProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FresherProjectService {

    @Autowired
    private FresherProjectRepository fresherProjectRepository;

    // Thêm mới FresherProject
    public FresherProject addFresherProject(FresherProject fresherProject) {
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
        FresherProject fresherProject = fresherProjectRepository.findById(id).orElseThrow(() -> new RuntimeException("FresherProject not found"));
        fresherProject.setFresher(fresherProjectDetails.getFresher());
        fresherProject.setProject(fresherProjectDetails.getProject());
        fresherProject.setRole(fresherProjectDetails.getRole());
        return fresherProjectRepository.save(fresherProject);
    }

    // Xóa FresherProject theo ID
    public void deleteFresherProject(int id) {
        fresherProjectRepository.deleteById(id);
    }
}
