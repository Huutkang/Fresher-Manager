package com.example.demo.configuration;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Configuration;

import com.example.demo.dto.response.UserResDto;
import com.example.demo.service.UsersService;

@Configuration
public class ApplicationInitConfig {
    private static final Logger log = LogManager.getLogger(UsersService.class);
    public ApplicationInitConfig(UsersService usersService){
        Optional<UserResDto> user = usersService.getUserById(1);
        if (user.isEmpty()) {
            // Sau khi chạy ứng dụng lần đầu tiên. Cần đăng nhập và thay đổi mật khẩu ngay.
            usersService.newAdmin("Admin", "123456");
            log.info("Đã khởi tạo thành công Admin với id = 1");
            System.out.println("Vui lòng đăng nhập và thay đổi mật khẩu ngay");
        }
    }
}