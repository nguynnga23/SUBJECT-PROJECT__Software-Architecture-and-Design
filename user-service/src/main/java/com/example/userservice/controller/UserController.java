package com.example.userservice.controller;

import com.example.userservice.dto.AuthenticationRequestDto;
import com.example.userservice.dto.AuthenticationResponseDto;
import com.example.userservice.dto.RefreshTokenRequestDto;
import com.example.userservice.dto.RefreshTokenResponseDto;
import com.example.userservice.entity.User;
import com.example.userservice.service.AuthenticationService;
import com.example.userservice.service.UserRedisService;
import com.example.userservice.service.UserService;
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
    public ResponseEntity<?> authenticate(@RequestBody final AuthenticationRequestDto authenticationRequestDto) {
        return authenticationService.authenticate(authenticationRequestDto);
    }
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String token) {
        String jwtToken = token.replace("Bearer ", ""); // Loại bỏ "Bearer " từ header
        authenticationService.logout(jwtToken);
        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<RefreshTokenResponseDto> refreshToken(@RequestBody RefreshTokenRequestDto refreshTokenRequest) {

        return ResponseEntity.ok(authenticationService.refreshToken(refreshTokenRequest));
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
