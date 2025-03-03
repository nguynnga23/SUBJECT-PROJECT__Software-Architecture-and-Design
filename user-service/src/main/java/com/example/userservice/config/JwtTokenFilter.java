package com.example.userservice.config;

import com.example.userservice.service.AuthenticationService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private  AuthenticationService authenticationService;
    @Autowired
    public void setAuthenticationService(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }
    @Autowired
    public JwtTokenFilter(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = extractToken(request);

        // Kiểm tra token có trong danh sách đen không
        if (token != null && authenticationService.isTokenBlacklisted(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token is blacklisted");
            return;
        }
        if (token != null && authenticationService.isTokenValid(token)) {
            // Lấy thông tin người dùng từ token và lưu vào request attribute
            String userId = authenticationService.getUserIdFromToken(token);
            request.setAttribute("userId", userId);
        }

        filterChain.doFilter(request, response);
    }

    // Trích xuất token từ header
    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // Loại bỏ "Bearer "
        }
        return null;
    }
}