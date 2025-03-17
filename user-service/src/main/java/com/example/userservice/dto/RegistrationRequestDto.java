package com.example.userservice.dto;

import com.example.userservice.enums.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegistrationRequestDto(
        @NotBlank(message = "Username is required")
        @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
        String username,

        @NotBlank(message = "Email is required")
        @Email(message = "Please provide a valid email address")
        String email,

        @JsonProperty("passwordHash")
        @NotBlank(message = "Password is required")
        @Size(min = 6, max = 30, message = "Password must be between 6 and 30 characters")
        String passwordHash,

        String fullName,

        Role role
) {
}