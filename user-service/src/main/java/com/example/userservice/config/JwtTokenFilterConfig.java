package com.example.userservice.config;

import com.example.userservice.service.AuthenticationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtTokenFilterConfig {

    @Bean
    public JwtTokenFilter jwtTokenFilter(AuthenticationService authenticationService) {
        return new JwtTokenFilter(authenticationService);
    }
}