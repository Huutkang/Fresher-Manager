package com.example.demo.configuration;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.service.AuthenticationService;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

@Component
public class TokenValidationFilter implements Filter {

    @Autowired
    private AuthenticationService authenticationService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("Filter initialized");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        System.out.println("Before servlet processing");

        // Giải mã token và lấy ID của token từ SecurityContext
        String tokenId = authenticationService.getTokenId();

        // Kiểm tra xem token có hợp lệ hay không
        boolean isActive = authenticationService.activeToken(tokenId);

        if (isActive) {
            // Tiếp tục chuỗi filter nếu token hợp lệ
            chain.doFilter(request, response);
        } else {
            // Xử lý khi token không hợp lệ, có thể trả về lỗi hoặc thông báo
            response.getWriter().write("Invalid token");
            response.getWriter().flush();
            return;
        }

        System.out.println("After servlet processing");
    }

    @Override
    public void destroy() {
        System.out.println("Filter destroyed");
    }
}
