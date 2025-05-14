package com.example.userservice.dto;

import com.example.userservice.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UserDTO {
    private UUID userId;

    private String email;

    private String fullName;

    private Role role;
}
