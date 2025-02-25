package com.example.userservice.dto;

public record AuthenticationResponseDto(String token,String email, String fullName) {

}