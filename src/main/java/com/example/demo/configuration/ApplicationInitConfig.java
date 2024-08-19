package com.example.demo.configuration;

import java.util.Optional;

import org.springframework.context.annotation.Configuration;

import com.example.demo.dto.response.UserResDto;
import com.example.demo.service.UsersService;




@Configuration
public class ApplicationInitConfig {
    
    public ApplicationInitConfig(UsersService usersService){
        Optional<UserResDto> user = usersService.getUserById(1);
        if (user.isEmpty()) {
            usersService.newAdmin("Admin", "123456");
            System.out.println("Admin đã tạo thành công");
        }
    }
}