package com.example.userservice.service;

import com.example.userservice.dto.AuthenticationRequestDto;
import com.example.userservice.dto.AuthenticationResponseDto;
import com.example.userservice.dto.RefreshTokenRequestDto;
import com.example.userservice.dto.RefreshTokenResponseDto;
import com.example.userservice.entity.User;
import com.example.userservice.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final Set<String> blacklistedTokens = new HashSet<>(); // Danh sách đen token
    private final PasswordEncoder passwordEncoder;

    //    Get userId By Token
public String getUserIdFromToken(String token) {
    try {
        // Giải mã token và trích xuất username (hoặc userId) từ token
        String username = jwtService.extractUsername(token);
        // Tìm user trong database bằng username
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Trả về userId của user
        return user.getUserId().toString(); // Giả sử userId là UUID và được lưu trong đối tượng User
    } catch (Exception e) {
        throw new InvalidBearerTokenException("Invalid token: " + e.getMessage());
    }
}
    //    Kiểm tra Token hợp lệ
    public boolean isTokenValid(String token) {
        try {
            // Giải mã token và kiểm tra tính hợp lệ
            String username = jwtService.extractUsername(token);
            return username != null && !isTokenBlacklisted(token);
        } catch (Exception e) {
            throw new InvalidBearerTokenException("Invalid token");
        }
    }
    //Đăng xuất
    public void logout(String token) {
        blacklistedTokens.add(token); // Thêm token vào danh sách đen
    }
    // Kiểm tra token có bị thu hồi không
    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokens.contains(token); // Kiểm tra token có trong danh sách đen không
    }

// Đăng nhập
public ResponseEntity<?> authenticate(final AuthenticationRequestDto request , HttpServletResponse response) {
    try {
        final var authToken = UsernamePasswordAuthenticationToken.unauthenticated(request.username(), request.passwordHash());
        authenticationManager.authenticate(authToken);
//        UUID id
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new RuntimeException("User not found"));
        // Tạo access token và refresh token
        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

//        User user = userRepository.findByUsername(request.username())
//                .orElseThrow(() -> new RuntimeException("User not found"));
        // Lưu Refresh Token vào HttpOnly Cookie
        Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(true); // Chỉ dùng HTTPS
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(7 * 24 * 60 * 60); // 7 ngày

        response.addCookie(refreshCookie);

        // Trả về Access Token + thông tin user (KHÔNG gửi Refresh Token)

        return ResponseEntity.ok(new AuthenticationResponseDto(accessToken, refreshToken, user.getEmail(), user.getFullName()));
    } catch (BadCredentialsException e) {
        return ResponseEntity.status(401).body(Map.of(
                "error", "Unauthorized",
                "message", "Wrong username or password!"
        ));
    } catch (AuthenticationException e) {
        return ResponseEntity.status(401).body(Map.of(
                "error", "Unauthorized",
                "message", "Authentication failed! Please check the information."
        ));
    } catch (Exception e) {
        return ResponseEntity.status(500).body(Map.of(
                "error", "Internal Server Error",
                "message", "Error during login. Please try again!"
        ));
    }
}
    public RefreshTokenResponseDto refreshToken(String  refreshToken) {
        // Giải mã refresh token và kiểm tra tính hợp lệ
        String username = jwtService.extractUsername(refreshToken);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Kiểm tra xem refresh token có hợp lệ không
        if (!isTokenValid(refreshToken)) {
            throw new InvalidBearerTokenException("Invalid refresh token");
        }

        // Tạo access token mới
        String newAccessToken = jwtService.generateToken(user);

        // Trả về response chứa access token mới và refresh token cũ
        return new RefreshTokenResponseDto(newAccessToken, refreshToken);
    }
    public void changePassword(UUID userId, String oldPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(oldPassword, user.getPasswordHash())) {
            throw new RuntimeException("Old password is incorrect");
        }

        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

}