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

import com.example.demo.dto.request.FresherReqDto;
import com.example.demo.dto.request.UpdateFresherReqDto;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.FresherResDto;
import com.example.demo.service.FresherService;

@RestController
@RequestMapping("/freshers")
public class FresherController {

    @Autowired
    private FresherService fresherService;

    // Thêm mới Fresher
    @PostMapping
    public ApiResponse createFresher(@RequestBody FresherReqDto fresherReqDto) {
        ApiResponse response = new ApiResponse();
        response.setResult(fresherService.addFresher(fresherReqDto));
        return response;
    }

    // Lấy tất cả Freshers
    @GetMapping
    public List<FresherResDto> getAllFreshers() {
        return fresherService.getAllFreshers();
    }

    // Lấy Fresher theo ID
    @GetMapping("/{id}")
    public ResponseEntity<FresherResDto> getFresherById(@PathVariable int id) {
        FresherResDto fresher = fresherService.getFresherById(id).orElse(null);
        if (fresher == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(fresher);
    }

    // Cập nhật Fresher
    @PutMapping("/{id}")
    public ResponseEntity<FresherResDto> updateFresher(@PathVariable int id, @RequestBody UpdateFresherReqDto req) {
        try {
            FresherResDto updatedFresher = fresherService.updateFresher(id, req);
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
