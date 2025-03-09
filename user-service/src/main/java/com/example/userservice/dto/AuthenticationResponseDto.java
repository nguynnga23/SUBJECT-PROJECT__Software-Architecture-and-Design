package com.example.userservice.dto;

public record AuthenticationResponseDto(String accessToken,String refreshToken,String email, String fullName) {

}