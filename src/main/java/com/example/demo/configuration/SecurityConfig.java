package com.example.demo.configuration;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    TokenValidationFilter tokenValidationFilter;

    private final static String SIGNER_KEY = "UqPgTaQLnqjwuOJ54TZnQekWcLyA+eR68BBKTULU/hD3IdIk5aHani1twPPQhlXf";

    private final String[] PUBLIC_GET_ENDPOINTS = {
        "/",
        "/home"
    };

    private final String[] PUBLIC_POST_ENDPOINTS = {
        "/users",
        "/auth/login",
        "/auth/introspect"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
            .authorizeHttpRequests(requests -> requests
                .requestMatchers(HttpMethod.GET, PUBLIC_GET_ENDPOINTS).hasAnyAuthority("SCOPE_ADMIN")
                .requestMatchers(HttpMethod.POST, PUBLIC_POST_ENDPOINTS).permitAll()
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwtConfigurer -> jwtConfigurer.decoder(jwtDecoder()))
            )
            .addFilterAfter(tokenValidationFilter, UsernamePasswordAuthenticationFilter.class)
            .csrf(AbstractHttpConfigurer::disable);
        
        return httpSecurity.build();
    }

    @Bean
    JwtDecoder jwtDecoder() {
        SecretKeySpec jwtSecretKeySpec = new SecretKeySpec(SIGNER_KEY.getBytes(), "HmacSHA512");
        return NimbusJwtDecoder
            .withSecretKey(jwtSecretKeySpec)
            .macAlgorithm(MacAlgorithm.HS512)
            .build();
    }
}
