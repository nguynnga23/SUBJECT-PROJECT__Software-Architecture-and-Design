package com.example.userservice.mapper;


import com.example.userservice.dto.RegistrationRequestDto;
import com.example.userservice.dto.RegistrationResponseDto;
import com.example.userservice.entity.User;
import org.springframework.stereotype.Component;


@Component
public class UserRegistrationMapper {

    public User toEntity(RegistrationRequestDto registrationRequestDto) {
        final var user = new User();

        user.setEmail(registrationRequestDto.email());
        user.setUsername(registrationRequestDto.username());
        user.setPasswordHash(registrationRequestDto.passwordHash());
        user.setFullName(registrationRequestDto.fullName());
        user.setRole(registrationRequestDto.role());

        return user;
    }

    public RegistrationResponseDto toRegistrationResponseDto(final User user) {
        return new RegistrationResponseDto( user.getUsername(),user.getEmail(), user.getFullName());
    }

}