package com.example.userservice.mapper;


import com.example.userservice.dto.AuthenticationResponseDto;
import com.example.userservice.dto.RegistrationResponseDto;
import com.example.userservice.dto.UserProfileDto;
import com.example.userservice.entity.User;
import org.springframework.stereotype.Component;


@Component
public class UserMapper {

    public UserProfileDto toUserProfileDto(final User user) {
        return new UserProfileDto(user.getEmail(), user.getUsername());
    }


}