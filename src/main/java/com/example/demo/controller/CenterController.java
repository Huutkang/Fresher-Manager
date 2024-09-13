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

import com.example.demo.dto.request.CenterReqDto;
import com.example.demo.dto.response.Api;
import com.example.demo.dto.response.CenterResDto;
import com.example.demo.enums.Code;
import com.example.demo.service.CenterService;

@RestController
@RequestMapping("/centers")
public class CenterController {

    @Autowired
    private CenterService centerService;

    // Thêm mới Center
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @PostMapping
    public ResponseEntity<Api<CenterResDto>> createCenter(@RequestBody CenterReqDto newCenter) {
        CenterResDto center = centerService.addCenter(newCenter);
        return Api.response(Code.OK, center);
    }

    // Lấy tất cả Centers
    @GetMapping
    public ResponseEntity<Api<List<CenterResDto>>> getAllCenters() {
        List<CenterResDto> centers = centerService.getAllCenters();
        return Api.response(Code.OK, centers);
    }

    // Lấy Center theo ID
    @GetMapping("/{id}")
    public ResponseEntity<Api<CenterResDto>> getCenterById(@PathVariable int id) {
        Optional<CenterResDto> center = centerService.getCenterById(id);
        if (center.isEmpty()) {
            return Api.response(Code.CENTER_NOT_EXISTED);
        }
        return Api.response(Code.OK, center.get());
    }

    // Cập nhật Center
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Api<CenterResDto>> updateCenter(@PathVariable int id, @RequestBody CenterReqDto req) {
        CenterResDto updatedCenter = centerService.updateCenter(id, req);
        return Api.response(Code.OK, updatedCenter);
    }

    // Xóa Center theo ID
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Api<Void>> deleteCenter(@PathVariable int id) {
        centerService.deleteCenter(id);
        return Api.response(Code.OK);
    }
}
