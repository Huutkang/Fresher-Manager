package com.example.demo.controller;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.request.ApiResponse;
import com.example.demo.dto.request.AuthenticationRequest;
import com.example.demo.dto.request.IntrospectRequest;
import com.example.demo.dto.response.AuthenticationResponse;
import com.example.demo.dto.response.IntrospectResponse;
import com.example.demo.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;


@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    @Autowired
    AuthenticationService authenticationService;
    @PostMapping("/login")
    public ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        var result = authenticationService.authenticate(request);
        ApiResponse<AuthenticationResponse> response = new ApiResponse<>();
        response.setResult(result);
        return response;
    }
    
    @PostMapping("/introspect")
    public ApiResponse<IntrospectResponse> authenticate(@RequestBody IntrospectRequest request) throws JOSEException, ParseException {
        var result = authenticationService.introspect(request);
        ApiResponse<IntrospectResponse> response = new ApiResponse<>();
        response.setResult(result);
        return response;
    }
    
}
