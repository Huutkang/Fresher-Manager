package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.FresherProject;
import com.example.demo.service.FresherProjectService;

@RestController
@RequestMapping("/fresherProjects")
public class FresherProjectController {

    @Autowired
    private FresherProjectService fresherProjectService;

    // Thêm mới FresherProject
    @PostMapping
    public FresherProject createFresherProject(@RequestBody FresherProject fresherProject) {
        return fresherProjectService.addFresherProject(fresherProject);
    }

    // Lấy tất cả FresherProjects
    @GetMapping
    public List<FresherProject> getAllFresherProjects() {
        return fresherProjectService.getAllFresherProjects();
    }

    // Lấy FresherProject theo ID
    @GetMapping("/{id}")
    public ResponseEntity<FresherProject> getFresherProjectById(@PathVariable int id) {
        FresherProject fresherProject = fresherProjectService.getFresherProjectById(id).orElse(null);
        if (fresherProject == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(fresherProject);
    }

    // Cập nhật FresherProject
    @PutMapping("/{id}")
    public ResponseEntity<FresherProject> updateFresherProject(@PathVariable int id, @RequestBody FresherProject fresherProjectDetails) {
        try {
            FresherProject updatedFresherProject = fresherProjectService.updateFresherProject(id, fresherProjectDetails);
            return ResponseEntity.ok(updatedFresherProject);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Xóa FresherProject theo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFresherProject(@PathVariable int id) {
        fresherProjectService.deleteFresherProject(id);
        return ResponseEntity.noContent().build();
    }
}
