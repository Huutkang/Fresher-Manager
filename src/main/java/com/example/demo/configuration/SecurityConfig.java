package com.example.demo.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final String[] PUBLIC_GET_ENDPOINTS = {
        "/users/*",
        "/users"
    };

    private final String[] PUBLIC_POST_ENDPOINTS = {
        "/auth/auth",
        "/auth/login",
        "/auth/introspect"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(requests -> requests.requestMatchers(HttpMethod.GET, PUBLIC_GET_ENDPOINTS).permitAll()
        .requestMatchers(HttpMethod.POST, PUBLIC_POST_ENDPOINTS).permitAll());
        

        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        return httpSecurity.build();
    }

}