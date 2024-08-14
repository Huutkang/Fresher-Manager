package com.example.demo.controller;

import com.example.demo.entity.Fresher;
import com.example.demo.service.FresherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/freshers")
public class FresherController {

    @Autowired
    private FresherService fresherService;

    // Thêm mới Fresher
    @PostMapping
    public Fresher createFresher(@RequestBody Fresher fresher) {
        return fresherService.addFresher(fresher);
    }

    // Lấy tất cả Freshers
    @GetMapping
    public List<Fresher> getAllFreshers() {
        return fresherService.getAllFreshers();
    }

    // Lấy Fresher theo ID
    @GetMapping("/{id}")
    public ResponseEntity<Fresher> getFresherById(@PathVariable int id) {
        Fresher fresher = fresherService.getFresherById(id).orElse(null);
        if (fresher == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(fresher);
    }

    // Cập nhật Fresher
    @PutMapping("/{id}")
    public ResponseEntity<Fresher> updateFresher(@PathVariable int id, @RequestBody Fresher fresherDetails) {
        try {
            Fresher updatedFresher = fresherService.updateFresher(id, fresherDetails);
            return ResponseEntity.ok(updatedFresher);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Xóa Fresher theo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFresher(@PathVariable int id) {
        fresherService.deleteFresher(id);
        return ResponseEntity.noContent().build();
    }
}
