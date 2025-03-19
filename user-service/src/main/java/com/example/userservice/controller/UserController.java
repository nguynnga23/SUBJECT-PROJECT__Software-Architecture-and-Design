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

import java.time.LocalDate;
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
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No Refresh Token.");
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

//    @PutMapping("/  ")
//    public ResponseEntity<User> updateUser(@PathVariable UUID userId, @RequestBody User user) {
//        return ResponseEntity.ok(userService.updateUser(userId, user));
//    }
    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable UUID userId,
                                        @RequestBody User user,
                                        @RequestHeader("Authorization") String authHeader) {
        try {
            // Kiểm tra Authorization Header có tồn tại không
            if (authHeader == null || authHeader.isBlank()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Authorization header is missing!"));
            }
            // Lấy Access Token từ Header
            String token = authHeader.replace("Bearer ", "");
            // Kiểm tra token có rỗng không
            if (token.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Token is empty!"));
            }
            // Kiểm tra Token hợp lệ
            if (!authenticationService.isTokenValid(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid token!"));
            }

            // Lấy userId từ token
            String userIdFromToken = authenticationService.getUserIdFromToken(token);

            if (userIdFromToken == null || !userIdFromToken.equals(userId.toString())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Unauthorized access!"));
            }

            // Lấy user hiện tại từ DB
            User existingUser = userService.getUserById(userId);
            if (existingUser == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "User not found!"));
            }

            // Nếu  null, giữ nguyên cũ
            if (user.getPasswordHash() == null || user.getPasswordHash().isEmpty()) {
                user.setPasswordHash(existingUser.getPasswordHash());
            }
            if(user.getUsername() == null || user.getUsername().isEmpty()) {
                user.setUsername(existingUser.getUsername());
            }
            if (user.getEmail() == null || user.getEmail().isEmpty()) {
                user.setEmail(existingUser.getEmail());
            }
            if(user.getFullName() == null || user.getFullName().isEmpty()) {
                user.setFullName(existingUser.getFullName());
            }
            if (user.getRole()==null || user.getRole().describeConstable().isEmpty() )
                user.setRole(existingUser.getRole());

            // Giu nguuyen created_at
            user.setCreatedAt(existingUser.getCreatedAt());
            // Cập nhật thông tin user
            user.setUpdatedAt(LocalDate.now());
            User updatedUser = userService.updateUser(userId, user);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An error occurred while updating the user."));
        }
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
