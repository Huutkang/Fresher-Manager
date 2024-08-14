package com.example.demo.controller;

import com.example.demo.entity.Center;
import com.example.demo.service.CenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/centers")
public class CenterController {

    @Autowired
    private CenterService centerService;

    // Thêm mới Center
    @PostMapping
    public Center createCenter(@RequestBody Center center) {
        return centerService.addCenter(center);
    }

    // Lấy tất cả Centers
    @GetMapping
    public List<Center> getAllCenters() {
        return centerService.getAllCenters();
    }

    // Lấy Center theo ID
    @GetMapping("/{id}")
    public ResponseEntity<Center> getCenterById(@PathVariable int id) {
        Center center = centerService.getCenterById(id).orElse(null);
        if (center == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(center);
    }

    // Cập nhật Center
    @PutMapping("/{id}")
    public ResponseEntity<Center> updateCenter(@PathVariable int id, @RequestBody Center centerDetails) {
        try {
            Center updatedCenter = centerService.updateCenter(id, centerDetails);
            return ResponseEntity.ok(updatedCenter);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Xóa Center theo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCenter(@PathVariable int id) {
        centerService.deleteCenter(id);
        return ResponseEntity.noContent().build();
    }
}
