package com.example.demo.controller;

import org.springframework.web.bind.annotation.RestController;
import com.example.demo.dto.request.EmailRequest;
import com.example.demo.dto.response.Api;
import com.example.demo.dto.response.CenterResDto;
import com.example.demo.enums.Code;
import com.example.demo.service.EmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;



@RestController
@RequestMapping("/email")
public class EmailController {

    @Autowired
    private EmailService emailService;
    // test email
    @RequestMapping("/send")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<Api<CenterResDto>> sendEmail(@RequestBody EmailRequest emailTest) {
        emailService.sendEmail(emailTest.getTo(), emailTest.getSubject(), emailTest.getText());
        return Api.response(Code.OK);
    }

}
