package com.example.demo.service;

import com.example.demo.entity.FresherProject;
import com.example.demo.exception.AppException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.repository.FresherProjectRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.demo.dto.request.FresherProjectReqDto;
import com.example.demo.dto.response.FresherProjectResDto;

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
        fresherProject.setFresher(fresherService.getFresher(fresher_id));
        fresherProject.setProject(projectService.getProject(project_id));
        fresherProject.setRole(role);
        return fresherProjectRepository.save(fresherProject);
    }

    public FresherProject addFresherProject(FresherProjectReqDto req) {
        FresherProject fresherProject = new FresherProject();
        fresherProject.setFresher(fresherService.getFresher(req.getIdFresher()));
        fresherProject.setProject(projectService.getProject(req.getIdProject()));
        fresherProject.setRole(req.getRole());
        return fresherProjectRepository.save(fresherProject);
    }

    // Lấy tất cả FresherProjects
    public List<FresherProjectResDto> getAllFresherProjects() {
        return fresherProjectRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    protected FresherProject getFresherProject(int id) {
        return fresherProjectRepository.findById(id)
        .orElseThrow(() -> new AppException(ErrorCode.FRESHERPROJECT_NOT_EXISTED));
    }

    public Optional<FresherProjectResDto> getFresherProjectById(int id) {
        try {
            return Optional.of(convertToDTO(getFresherProject(id)));
        } catch (RuntimeException e) {
            return Optional.empty();
        }
    }

    // Cập nhật FresherProject
    public FresherProject updateFresherProject(int id, FresherProjectReqDto req) {
        FresherProject fresherProject = getFresherProject(id);
        fresherProject.setFresher(fresherService.getFresher(req.getIdFresher()));
        fresherProject.setProject(projectService.getProject(req.getIdProject()));
        fresherProject.setRole(req.getRole());
        return fresherProjectRepository.save(fresherProject);
    }

    // Xóa FresherProject theo ID
    public void deleteFresherProject(int id) {
        fresherProjectRepository.deleteById(id);
    }

    public List<FresherProject> findFresherProjects(Integer fresherId, Integer projectId) {
        if (fresherId > 0 && projectId > 0) {
            return fresherProjectRepository.findByFresherIdAndProjectId(fresherId, projectId);
        } else if (fresherId > 0) {
            return fresherProjectRepository.findByFresherId(fresherId);
        } else if (projectId > 0) {
            return fresherProjectRepository.findByProjectId(projectId);
        } else {
            throw new IllegalArgumentException("Phải cung cấp ít nhất một trong số FresherId hoặc projectId");
        }
    }

    protected List<FresherProject> findByRole(String role){
        return fresherProjectRepository.findByRole(role);
    }

    protected FresherProjectResDto convertToDTO(FresherProject fresherProject) {
        FresherProjectResDto res = new FresherProjectResDto();
        res.setId(fresherProject.getId());
        res.setIdFresher(fresherProject.getFresher().getId());
        res.setIdProject(fresherProject.getProject().getId());
        res.setFresher(fresherProject.getFresher().getName());
        res.setProject(fresherProject.getProject().getName());
        res.setRole(fresherProject.getRole());
        return res;
    }
}
