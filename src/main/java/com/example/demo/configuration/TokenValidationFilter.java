package com.example.demo.configuration;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.dto.response.ApiResponse;
import com.example.demo.exception.ErrorCode;
import com.example.demo.service.AuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class TokenValidationFilter implements Filter {
    @Autowired
    private AuthenticationService authenticationService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String tokenId;
        try{
            tokenId = authenticationService.getTokenId();
        }catch(Exception e){
            chain.doFilter(request, response);
            return;
        }
        boolean isActive = authenticationService.activeToken(tokenId);
        if (!isActive) {
            ErrorCode errorCode = ErrorCode.UNAUTHENTICATED;
            ApiResponse apiResponse = new ApiResponse();
            apiResponse.setCode(errorCode.getCode());
            apiResponse.setMessage(errorCode.getMessage());

            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setContentType("application/json;charset=UTF-8");
            httpResponse.setStatus(errorCode.getStatusCode().value());
            httpResponse.getWriter().write(new ObjectMapper().writeValueAsString(apiResponse));
            return;
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
