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

import com.example.demo.dto.request.FresherReqDto;
import com.example.demo.dto.request.UpdateFresherReqDto;
import com.example.demo.dto.response.Api;
import com.example.demo.dto.response.FresherResDto;
import com.example.demo.enums.Code;
import com.example.demo.service.AuthenticationService;
import com.example.demo.service.FresherService;

@RestController
@RequestMapping("/freshers")
public class FresherController {

    @Autowired
    private FresherService fresherService;

    @Autowired
    AuthenticationService authenticationService;

    // Thêm mới Fresher
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @PostMapping
    public ResponseEntity<Api<FresherResDto>> createFresher(@RequestBody FresherReqDto fresherReqDto) {
        FresherResDto fresher = fresherService.addFresher(fresherReqDto);
        return Api.response(Code.OK, fresher);
    }

    // Lấy tất cả Freshers
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @GetMapping
    public ResponseEntity<Api<List<FresherResDto>>> getAllFreshers() {
        List<FresherResDto> freshers = fresherService.getAllFreshers();
        return Api.response(Code.OK, freshers);
    }

    // Lấy Fresher theo ID
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Api<FresherResDto>> getFresherById(@PathVariable int id) {
        Optional<FresherResDto> fresher = fresherService.getFresherById(id);
        if (fresher.isEmpty()) {
            return Api.response(Code.FRESHER_NOT_EXISTED);
        }
        return Api.response(Code.OK, fresher.get());
    }

    // Cập nhật Fresher
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Api<FresherResDto>> updateFresher(@PathVariable int id, @RequestBody UpdateFresherReqDto req) {
        FresherResDto updatedFresher = fresherService.updateFresher(id, req);
        return Api.response(Code.OK, updatedFresher);
    }
    
    // Xóa Fresher theo ID
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Api<Void>> deleteFresher(@PathVariable int id) {
        fresherService.deleteFresher(id);
        return Api.response(Code.OK);
    }
    
    @PreAuthorize("hasAuthority('SCOPE_FRESHER')")
    @GetMapping("/me")
    public ResponseEntity<Api<FresherResDto>> getFresherById() {
        FresherResDto fresher = fresherService.getFresherByUserId(authenticationService.getIdUser());
        return Api.response(Code.OK, fresher);
    }

    @PreAuthorize("hasAuthority('SCOPE_FRESHER')")
    @PutMapping("/me")
    public ResponseEntity<Api<FresherResDto>> updateFresher(@RequestBody UpdateFresherReqDto req) {
        try {
            int id = fresherService.getFresherByUserId(authenticationService.getIdUser()).getId();
            FresherResDto updatedFresher = fresherService.updateFresher(id, req);
            return Api.response(Code.OK, updatedFresher);
        } catch (RuntimeException e) {
            return Api.response(Code.FRESHER_NOT_EXISTED);
        }
    }
}
