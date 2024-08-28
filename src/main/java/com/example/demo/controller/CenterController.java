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

import com.example.demo.dto.request.CenterReqDto;
import com.example.demo.dto.response.CenterResDto;
import com.example.demo.entity.Center;
import com.example.demo.service.CenterService;

@RestController
@RequestMapping("/centers")
public class CenterController {

    @Autowired
    private CenterService centerService;

    // Thêm mới Center
    @PostMapping
    public Center createCenter(@RequestBody CenterReqDto newCenter) {
        return centerService.addCenter(newCenter);
    }

    // Lấy tất cả Centers
    @GetMapping
    public List<CenterResDto> getAllCenters() {
        return centerService.getAllCenters();
    }

    // Lấy Center theo ID
    @GetMapping("/{id}")
    public ResponseEntity<CenterResDto> getCenterById(@PathVariable int id) {
        CenterResDto center = centerService.getCenterById(id).orElse(null);
        if (center == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(center);
    }

    // Cập nhật Center
    @PutMapping("/{id}")
    public ResponseEntity<CenterResDto> updateCenter(@PathVariable int id, @RequestBody Center centerDetails) {
        try {
            CenterResDto updatedCenter = centerService.updateCenter(id, centerDetails);
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
