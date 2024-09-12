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

import com.example.demo.dto.request.NewUserReqDto;
import com.example.demo.dto.request.SetUserReqDto;
import com.example.demo.dto.response.Api;
import com.example.demo.dto.response.UserResDto;
import com.example.demo.enums.Code;
import com.example.demo.enums.Role;
import com.example.demo.exception.AppException;
import com.example.demo.service.AuthenticationService;
import com.example.demo.service.UsersService;

@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UsersService usersService;

    @Autowired
    AuthenticationService authenticationService;

    // Thêm mới User
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @PostMapping
    public ResponseEntity<Api<UserResDto>> createUser(@RequestBody NewUserReqDto newUserReq) {
        UserResDto user = usersService.addUser(newUserReq);
        return Api.response(Code.OK, user);
    }

    // Lấy tất cả Users
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @GetMapping
    public ResponseEntity<Api<List<UserResDto>>> getAllUsers() {
        List<UserResDto> users = usersService.getAllUsers();
        return Api.response(Code.OK, users);
    }

    // Lấy User theo ID
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Api<UserResDto>> getUserById(@PathVariable int id) {
        Optional<UserResDto> user = usersService.getUserById(id);
        if (user.isEmpty()) {
            return Api.response(Code.USER_NOT_EXISTED);
        }
        return Api.response(Code.OK, user.get());
    }

    // Cập nhật User
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Api<UserResDto>> updateUser(@PathVariable int id, @RequestBody SetUserReqDto userDetails) {
        UserResDto updatedUser = usersService.updateUser(id, userDetails);
        return Api.response(Code.OK, updatedUser);
    }

    // Xóa User theo ID
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Api<Void>> deleteUser(@PathVariable int id) {
        usersService.deleteUser(id);
        return Api.response(Code.OK);
    }

    @GetMapping("/me")
    public ResponseEntity<Api<UserResDto>> getUserById() {
        int id = authenticationService.getIdUser();
        Optional<UserResDto> user = usersService.getUserById(id);
        if (user.isEmpty()) {
            throw new AppException(Code.UNAUTHENTICATED);
        }
        return Api.response(Code.OK, user.get());
    }
    
    // Cập nhật User
    @PutMapping("/me")
    public ResponseEntity<Api<UserResDto>> updateUser(@RequestBody SetUserReqDto userDetails) {
        int id = authenticationService.getIdUser();
        try {
            UserResDto updatedUser = usersService.updateUser(id, userDetails);
            return Api.response(Code.OK, updatedUser);
        } catch (RuntimeException e) {
            throw new AppException(Code.UNAUTHENTICATED);
        }
    }

    // Thêm role cho User
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @PutMapping("/{id}/addrole")
    public ResponseEntity<Api<Void>> addRoleToUser(@PathVariable int id, @RequestBody Role role) {
        usersService.addRole(id, role);
        return Api.response(Code.OK);
    }

    // Xóa role khỏi User
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @PutMapping("/{id}/removerole")
    public ResponseEntity<Api<Void>> removeRoleFromUser(@PathVariable int id, @RequestBody Role role) {
        usersService.removeRole(id, role);
        return Api.response(Code.OK);
    }

    // Cập nhật mật khẩu User
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @PutMapping("/{id}/setpassword")
    public ResponseEntity<Api<Void>> setPassword(@PathVariable int id, @RequestBody String newPassword) {
        newPassword = newPassword.substring(1, newPassword.length() - 1);
        boolean updated = usersService.updatePassword(id, newPassword);
        if (!updated) {
            return Api.response(Code.USER_PASSWORD_UPDATE_FAILED);
        }
        return Api.response(Code.OK);
    }

    @PutMapping("/updatepassword")
    public ResponseEntity<Api<Void>> updatePassword(@RequestBody String newPassword) {
        newPassword = newPassword.substring(1, newPassword.length() - 1);
        int id = authenticationService.getIdUser();
        boolean updated = usersService.updatePassword(id, newPassword);
        if (!updated) {
            return Api.response(Code.USER_PASSWORD_UPDATE_FAILED);
        }
        return Api.response(Code.OK);
    }
}
