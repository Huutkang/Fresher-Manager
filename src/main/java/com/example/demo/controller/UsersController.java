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

import com.example.demo.entity.Users;
import com.example.demo.service.UsersService;
import com.example.demo.dto.request.NewUserReqDto;
import com.example.demo.dto.request.SetUserReqDto;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.UserResDto;

@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UsersService usersService;

    // Thêm mới User
    @PostMapping
    public ApiResponse createUser(@RequestBody NewUserReqDto newUserReq) {
        ApiResponse response = new ApiResponse();
        response.setResult(usersService.convertToDTO(usersService.addUser(newUserReq)));
        return response;
    }

    // Lấy tất cả Users
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @GetMapping
    public List<UserResDto> getAllUsers() {
        return usersService.getAllUsers();
    }

    // Lấy User theo ID
    @GetMapping("/{id}")
    public ResponseEntity<UserResDto> getUserById(@PathVariable int id) {
        Optional<UserResDto> user = usersService.getUserById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user.get());
    }

    // Cập nhật User
    @PutMapping("/{id}")
    public ResponseEntity<Users> updateUser(@PathVariable int id, @RequestBody SetUserReqDto userDetails) {
        try {
            Users updatedUser = usersService.updateUser(id, userDetails);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Xóa User theo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable int id) {
        usersService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
