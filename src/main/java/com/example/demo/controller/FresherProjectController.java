package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

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

import com.example.demo.dto.request.FresherProjectReqDto;
import com.example.demo.dto.response.Api;
import com.example.demo.dto.response.FresherProjectResDto;
import com.example.demo.enums.Code;
import com.example.demo.service.EmailService;
import com.example.demo.service.FresherProjectService;
import com.example.demo.service.FresherService;

@RestController
@RequestMapping("/fresherprojects")
public class FresherProjectController {

    @Autowired
    private FresherProjectService fresherProjectService;

    @Autowired
    private FresherService fresherService;

    @Autowired
    private EmailService emailService;

    // Thêm mới FresherProject
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @PostMapping
    public ResponseEntity<Api<String>> createFresherProject(@RequestBody FresherProjectReqDto fresherProject) {
        emailService.projectAnnouncement(
            fresherService.getFresherById(fresherProject.getIdFresher()).get().getIdUser(), 
            fresherProject.getIdProject(), 
            true
        );
        fresherProjectService.addFresherProject(fresherProject);
        return Api.response(Code.OK, "Thêm mới FresherProject thành công");
    }

    // Lấy tất cả FresherProjects
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @GetMapping
    public ResponseEntity<Api<List<FresherProjectResDto>>> getAllFresherProjects() {
        List<FresherProjectResDto> fresherProjects = fresherProjectService.getAllFresherProjects();
        return Api.response(Code.OK, fresherProjects);
    }

    // Lấy FresherProject theo ID
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Api<FresherProjectResDto>> getFresherProjectById(@PathVariable int id) {
        Optional<FresherProjectResDto> fresherProject = fresherProjectService.getFresherProjectById(id);
        if (fresherProject.isEmpty()) {
            return Api.response(Code.FRESHERPROJECT_NOT_EXISTED);
        }
        return Api.response(Code.OK, fresherProject.get());
    }

    // Cập nhật FresherProject
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Api<FresherProjectResDto>> updateFresherProject(@PathVariable int id, @RequestBody FresherProjectReqDto fresherProjectDetails) {
        try {
            FresherProjectResDto updatedFresherProject = fresherProjectService.updateFresherProject(id, fresherProjectDetails);
            return Api.response(Code.OK, updatedFresherProject);
        } catch (RuntimeException e) {
            return Api.response(Code.FRESHERPROJECT_NOT_EXISTED);
        }
    }

    // Xóa FresherProject theo ID
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Api<String>> deleteFresherProject(@PathVariable int id) {
        Optional<FresherProjectResDto> frsprj = fresherProjectService.getFresherProjectById(id);
        if (frsprj.isPresent()) {
            emailService.projectAnnouncement(
                fresherService.getFresherById(frsprj.get().getIdFresher()).getIdUser(), 
                frsprj.get().getIdProject(), 
                false
            );
            fresherProjectService.deleteFresherProject(id);
            return Api.response(Code.OK, "Xóa FresherProject thành công");
        } else {
            return Api.response(Code.FRESHERPROJECT_NOT_EXISTED);
        }
    }
}
