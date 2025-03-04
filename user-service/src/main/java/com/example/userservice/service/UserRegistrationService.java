package com.example.userservice.service;

import com.example.userservice.dto.RegistrationRequestDto;
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
    public User registerUser(RegistrationRequestDto registrationRequestDto) {
        final Map<String, String> errors = new HashMap<>();

        // Validate username
        if (registrationRequestDto.username() == null || registrationRequestDto.username().isBlank()) {
            errors.put("username", "Username cannot be empty");
        }

        // Validate email
        if (registrationRequestDto.email() == null || registrationRequestDto.email().isBlank()) {
            errors.put("email", "Email cannot be empty");
        }

        // Validate password
        if (registrationRequestDto.passwordHash() == null || registrationRequestDto.passwordHash().length() < 6) {
            errors.put("passwordHash", "Password must be at least 6 characters");
        }

        // Check if email is already taken
        if (userRepository.existsByEmail(registrationRequestDto.email().toLowerCase())) {
            errors.put("email", "Email [%s] is already taken".formatted(registrationRequestDto.email()));
        }

        // Check if username is already taken
        if (userRepository.existsByUsername(registrationRequestDto.username())) {
            errors.put("username", "Username [%s] is already taken".formatted(registrationRequestDto.username()));
        }

        // If there are validation errors, throw an exception
        if (!errors.isEmpty()) {
            logger.error("Registration failed for user: {}", registrationRequestDto);
            logger.error("Errors: {}", errors);
            throw new ValidationException(CONFLICT, errors, registrationRequestDto);
        }

        // Create a new User entity from the RegistrationRequestDto
        User user = new User();
        user.setUsername(registrationRequestDto.username());
        user.setEmail(registrationRequestDto.email().toLowerCase()); // Normalize email
        user.setPasswordHash(passwordEncoder.encode(registrationRequestDto.passwordHash())); // Encode password
        user.setFullName(registrationRequestDto.fullName());
        user.setRole(registrationRequestDto.role() != null ? registrationRequestDto.role() : Role.USER); // Set default role if not provided

        // Save the user to the database
        User savedUser = userRepository.save(user);
        logger.info("User [{}] registered successfully", savedUser.getUsername());

        return savedUser;
    }
}