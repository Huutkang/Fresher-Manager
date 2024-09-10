package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.request.ProjectReqDto;
import com.example.demo.dto.response.Api;
import com.example.demo.dto.response.ProjectResDto;
import com.example.demo.enums.Code;
import com.example.demo.service.ProjectService;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    // Thêm mới Project
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @PostMapping
    public ResponseEntity<Api<ProjectResDto>> createProject(@RequestBody ProjectReqDto req) {
        ProjectResDto project = projectService.addProject(req);
        return Api.response(Code.OK, project);
    }

    // Lấy tất cả Projects
    @GetMapping
    public ResponseEntity<Api<List<ProjectResDto>>> getAllProjects() {
        List<ProjectResDto> projects = projectService.getAllProjects();
        return Api.response(Code.OK, projects);
    }

    // Lấy Project theo ID
    @GetMapping("/{id}")
    public ResponseEntity<Api<ProjectResDto>> getProjectById(@PathVariable int id) {
        return projectService.getProjectById(id)
                .map(project -> Api.response(Code.OK, project))
                .orElseGet(() -> Api.response(Code.PROJECT_NOT_EXISTED));
    }

    // Cập nhật Project
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Api<ProjectResDto>> updateProject(@PathVariable int id, @RequestBody ProjectReqDto projectDetails) {
        try {
            ProjectResDto updatedProject = projectService.updateProject(id, projectDetails);
            return Api.response(Code.OK, updatedProject);
        } catch (RuntimeException e) {
            return Api.response(Code.PROJECT_NOT_EXISTED);
        }
    }

    // Xóa Project theo ID
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Api<Void>> deleteProject(@PathVariable int id) {
        projectService.deleteProject(id);
        return Api.response(Code.OK);
    }
}
