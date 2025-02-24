package com.example.userservice.dto;

public record RegistrationResponseDto(
        String username,
        String email,
        String fullName
) {
}