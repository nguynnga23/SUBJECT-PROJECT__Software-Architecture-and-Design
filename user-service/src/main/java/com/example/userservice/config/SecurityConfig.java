package com.example.userservice.config;

import com.example.userservice.service.AuthenticationService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity http,JwtTokenFilter jwtTokenFilter,CustomAuthenticationEntryPoint authEntryPoint ) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable) // Tắt CSRF
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/user-service/users/register", "/api/v1/user-service/users/login", "/api/v1/user-service/users/refresh-token","/api/v1/user-service/users/**").permitAll() // Cho phép truy cập công khai
                        .anyRequest().authenticated() // Yêu cầu xác thực cho các endpoint khác
                )
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS)) // Không sử dụng session
                .oauth2ResourceServer(server -> server
                        .jwt(Customizer.withDefaults()) // Sử dụng JWT
                        .authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint()) // Xử lý lỗi xác thực
                        .accessDeniedHandler(new BearerTokenAccessDeniedHandler()) // Xử lý lỗi truy cập bị từ chối
                )
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class) // Thêm JwtTokenFilter
//                .exceptionHandling(ex -> ex
//                        .authenticationEntryPoint((request, response, authException) -> {
//                            response.setContentType("application/json");
//                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                            response.getWriter().write("{\"error\": \"Unauthorized access!\"}");
//                        })
//                        .accessDeniedHandler((request, response, accessDeniedException) -> {
//                            response.setContentType("application/json");
//                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//                            response.getWriter().write("{\"error\": \"Access denied!\"}");
//                        })
//                )
                .exceptionHandling(ex -> ex.authenticationEntryPoint(authEntryPoint))
                .build(); // Xây dựng SecurityFilterChain
    }

    @Bean
    public AuthenticationManager authenticationManager(final AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}