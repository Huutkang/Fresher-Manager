package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.request.AuthenticationRequest;
import com.example.demo.dto.response.Api;
import com.example.demo.dto.response.AuthenticationResponse;
import com.example.demo.enums.Code;
import com.example.demo.service.AuthenticationService;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    // Đăng nhập
    @PostMapping("/login")
    public ResponseEntity<Api<AuthenticationResponse>> authenticate(@RequestBody AuthenticationRequest request) {
        if (authenticationService.checklogin()) {
            return Api.response(Code.LOGIN_SUCCESSFULLY);
        } else {
            AuthenticationResponse result = authenticationService.authenticate(request);
            return Api.response(Code.OK, result);
        }
    }

    // Đăng xuất
    @RequestMapping("/logout")
    public ResponseEntity<Api<Void>> logout() {
        authenticationService.logout();
        return Api.response(Code.OK);
    }

    // Làm mới token
    @PostMapping("/refreshtoken")
    public ResponseEntity<Api<AuthenticationResponse>> refreshToken() {
        AuthenticationResponse result = authenticationService.refreshToken();
        return Api.response(Code.OK, result);
    }

    // Kiểm tra trạng thái đăng nhập
    @RequestMapping("/checklogin")
    public ResponseEntity<Api<String>> checkLogin() {
        if (authenticationService.checklogin()) {
            return Api.response(Code.LOGGED_IN);
        } else {
            return Api.response(Code.UNAUTHENTICATED);
        }
    }
}
