package com.example.demo.service;

import com.example.demo.entity.FresherProject;
import com.example.demo.enums.Code;
import com.example.demo.exception.AppException;
import com.example.demo.repository.FresherProjectRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.demo.dto.request.FresherProjectReqDto;
import com.example.demo.dto.response.FresherProjectResDto;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



@Service
public class FresherProjectService {

    @Autowired
    private FresherProjectRepository fresherProjectRepository;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private FresherService fresherService;

    private static final Logger log = LogManager.getLogger(FresherProjectService.class);

    // Thêm mới FresherProject
    public FresherProject addFresherProject(int fresher_id, int project_id, String role) {
        FresherProject fresherProject = new FresherProject();
        fresherProject.setFresher(fresherService.getFresher(fresher_id));
        fresherProject.setProject(projectService.getProject(project_id));
        fresherProject.setRole(role);
        log.info("Add FresherProject");
        return fresherProjectRepository.save(fresherProject);
    }

    public FresherProject addFresherProject(FresherProjectReqDto req) {
        FresherProject fresherProject = new FresherProject();
        fresherProject.setFresher(fresherService.getFresher(req.getIdFresher()));
        fresherProject.setProject(projectService.getProject(req.getIdProject()));
        fresherProject.setRole(req.getRole());
        log.info("Add FresherProject");
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
        .orElseThrow(() -> new AppException(Code.FRESHERPROJECT_NOT_EXISTED));
    }

    public Optional<FresherProjectResDto> getFresherProjectById(int id) {
        try {
            return Optional.of(convertToDTO(getFresherProject(id)));
        } catch (RuntimeException e) {
            return Optional.empty();
        }
    }

    // Cập nhật FresherProject
    public FresherProjectResDto updateFresherProject(int id, FresherProjectReqDto req) {
        FresherProject fresherProject = getFresherProject(id);
        fresherProject.setFresher(fresherService.getFresher(req.getIdFresher()));
        fresherProject.setProject(projectService.getProject(req.getIdProject()));
        fresherProject.setRole(req.getRole());
        log.info("Update FresherProject", id);
        return convertToDTO(fresherProjectRepository.save(fresherProject));
    }

    // Xóa FresherProject theo ID
    public void deleteFresherProject(int id) {
        fresherProjectRepository.deleteById(id);
        log.info("Delete FresherProject", id);
    }

    // Tìm kiếm FresherProjects theo fresherId và projectId, trả về DTO
    public List<FresherProjectResDto> findFresherProjects(Integer fresherId, Integer projectId) {
        List<FresherProject> fresherProjects;
        if (fresherId > 0 && projectId > 0) {
            fresherProjects = fresherProjectRepository.findByFresherIdAndProjectId(fresherId, projectId);
        } else if (fresherId > 0) {
            fresherProjects = fresherProjectRepository.findByFresherId(fresherId);
        } else if (projectId > 0) {
            fresherProjects = fresherProjectRepository.findByProjectId(projectId);
        } else {
            throw new IllegalArgumentException("Phải cung cấp ít nhất một trong số FresherId hoặc projectId");
        }
        return fresherProjects.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Tìm kiếm FresherProjects theo role, trả về DTO
    public List<FresherProjectResDto> findByRole(String role) {
        return fresherProjectRepository.findByRole(role).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
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
