package com.example.userservice.service;

import com.example.userservice.entity.User;
import com.example.userservice.enums.Role;
import com.example.userservice.exception.ValidationException;
import com.example.userservice.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.CONFLICT;

@Service
@RequiredArgsConstructor
public class UserRegistrationService {
    private static final Logger logger = LoggerFactory.getLogger(UserRegistrationService.class); // Sửa lại tên class
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User registerUser(User user) {
        final Map<String, String> errors = new HashMap<>();

        if (user.getUsername() == null || user.getUsername().isBlank()) {
            errors.put("username", "Username cannot be empty");
        }

        if (user.getEmail() == null || user.getEmail().isBlank()) {
            errors.put("email", "Email cannot be empty");
        } else {
            user.setEmail(user.getEmail().toLowerCase()); // Chuẩn hóa email
        }

        if (user.getPasswordHash() == null || user.getPasswordHash().length() < 6) {
            errors.put("passwordHash", "Password must be at least 6 characters");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            errors.put("email", "Email [%s] is already taken".formatted(user.getEmail()));
        }

        if (userRepository.existsByUsername(user.getUsername())) {
            errors.put("username", "Username [%s] is already taken".formatted(user.getUsername()));
        }

        if (!errors.isEmpty()) {
            // Ghi log body khi đăng ký thất bại
            logger.error("Registration failed for user: {}", user);
            logger.error("Errors: {}", errors);
            throw new ValidationException(CONFLICT, errors,user);
        }

        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));

        if (user.getRole() == null) {
            user.setRole(Role.USER); // Gán role mặc định nếu không có
        }

        User savedUser = userRepository.save(user);
        logger.info("User [{}] registered successfully", savedUser.getUsername());

        return savedUser;
    }
}