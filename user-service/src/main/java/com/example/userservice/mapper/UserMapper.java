package com.example.userservice.mapper;


import com.example.userservice.dto.AuthenticationResponseDto;
import com.example.userservice.dto.RegistrationResponseDto;
import com.example.userservice.dto.UserDTO;
import com.example.userservice.dto.UserProfileDto;
import com.example.userservice.entity.User;
import org.springframework.stereotype.Component;


@Component
public class UserMapper {

    public UserProfileDto toUserProfileDto(final User user) {
        return new UserProfileDto(user.getEmail(), user.getUsername());
    }

    public UserDTO toUserDTO(final User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(user.getEmail());
        userDTO.setUserId(user.getUserId());
        userDTO.setRole(user.getRole());
        userDTO.setFullName(user.getFullName());
        return userDTO;
    }
}