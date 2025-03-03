package com.example.userservice.controller;

import com.example.userservice.dto.AuthenticationRequestDto;
import com.example.userservice.dto.AuthenticationResponseDto;
import com.example.userservice.dto.RefreshTokenRequestDto;
import com.example.userservice.dto.RefreshTokenResponseDto;
import com.example.userservice.entity.User;
import com.example.userservice.service.AuthenticationService;
import com.example.userservice.service.JwtService;
import com.example.userservice.service.UserRedisService;
import com.example.userservice.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private JwtService jwtService;

    @GetMapping("/profile/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable UUID userId,HttpServletRequest request) {
        // Lấy userId từ token
        String userIdFromToken = (String) request.getAttribute("userId");

        if (userIdFromToken == null || !userIdFromToken.equals(userId.toString())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Unauthorized access"));
        }
        User user = userService.getUserById(userId);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "User not found"));
        }
    }

    // As a registry --- Nguyen Chung must change createUser

//    @PostMapping("/login")
//    public ResponseEntity<?> authenticate(@RequestBody final AuthenticationRequestDto authenticationRequestDto) {
//        return authenticationService.authenticate(authenticationRequestDto);
//    }
@PostMapping("/login")
public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequestDto authenticationRequestDto,
                                      HttpServletResponse response) {
    // Gọi service để xác thực người dùng
    ResponseEntity<?> authResponse = authenticationService.authenticate(authenticationRequestDto, response);

    if (authResponse.getBody() instanceof AuthenticationResponseDto authBody) {
        // Lấy Refresh Token từ response body
        String refreshToken = authBody.refreshToken();

        // Lưu Refresh Token vào HttpOnly Cookie
        Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(true); // Chỉ dùng HTTPS
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(7 * 24 * 60 * 60); // 7 ngày

        response.addCookie(refreshCookie);

        // Chỉ trả về Access Token + thông tin user
        return ResponseEntity.ok(Map.of(
                "accessToken", authBody.accessToken(),
                "email", authBody.email(),
                "fullName", authBody.fullName()
        ));
    }

    // Trả về lỗi nếu response không hợp lệ
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error","User or password wrong"));
}

//    @PostMapping("/logout")
//    public ResponseEntity<?> logout(@RequestHeader("Authorization") String token) {
//        String jwtToken = token.replace("Bearer ", ""); // Loại bỏ "Bearer " từ header
//        authenticationService.logout(jwtToken);
//        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
//    }
@PostMapping("/logout")
public ResponseEntity<?> logout(@RequestHeader("Authorization") String token,
                                HttpServletResponse response) {
    // Loại bỏ "Bearer " để lấy JWT Token
    String jwtToken = token.replace("Bearer ", "");

    // Gọi service để xử lý logout (xóa token khỏi DB nếu có)
    authenticationService.logout(jwtToken);

    // Xóa Refresh Token khỏi Cookie
    Cookie refreshTokenCookie = new Cookie("refreshToken", "");
    refreshTokenCookie.setHttpOnly(true);
    refreshTokenCookie.setSecure(true); // Chỉ dùng HTTPS
    refreshTokenCookie.setPath("/");
    refreshTokenCookie.setMaxAge(0); // Hết hạn ngay lập tức
    response.addCookie(refreshTokenCookie);

    return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
}

//    @PostMapping("/refresh-token")
//    public ResponseEntity<RefreshTokenResponseDto> refreshToken(@RequestHeader("Authorization") String authHeader) {
//        // Loại bỏ "Bearer " từ header
//        String refreshToken = authHeader.replace("Bearer ", "");
//
//        // Gọi service để refresh token
//        RefreshTokenResponseDto response = authenticationService.refreshToken(refreshToken);
//        return ResponseEntity.ok(response);
//    }
@GetMapping("/protected-api")
public ResponseEntity<?> getProtectedData(@RequestHeader("Authorization") String authHeader) {
    try {
        // Lấy Access Token từ Header
        String token = authHeader.replace("Bearer ", "");

        // Xác thực Token
        if (!authenticationService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token!");
        }

        // Giả sử lấy thông tin user từ token
        String username = jwtService.extractUsername(token);

        return ResponseEntity.ok(Map.of(
                "message", "You have accessed a protected API!",
                "username", username
        ));
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized!");
    }
}
@PostMapping("/refresh-token")
public ResponseEntity<?> refreshToken(@CookieValue(value = "refreshToken", required = false) String refreshToken,
                                      HttpServletResponse response) {
    if (refreshToken == null || refreshToken.isEmpty()) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Không có Refresh Token.");
    }

    // Gọi service để refresh token
    RefreshTokenResponseDto newTokens = authenticationService.refreshToken(refreshToken);

    // Cập nhật Refresh Token mới vào Cookie (nếu cần)
    Cookie refreshCookie = new Cookie("refreshToken", newTokens.refreshToken());
    refreshCookie.setHttpOnly(true);
    refreshCookie.setSecure(true);
    refreshCookie.setPath("/");
    refreshCookie.setMaxAge(7 * 24 * 60 * 60);

    response.addCookie(refreshCookie);

    return ResponseEntity.ok(Map.of("accessToken", newTokens.accessToken()));
}

    @PutMapping("/  ")
    public ResponseEntity<User> updateUser(@PathVariable UUID userId, @RequestBody User user) {
        return ResponseEntity.ok(userService.updateUser(userId, user));
    }


    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable UUID userId) {
        boolean isDeleted = userService.deleteUser(userId);

        if (isDeleted) {
            return ResponseEntity.ok(Map.of("message", "User deleted successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "User not found"));
        }
    }
}
