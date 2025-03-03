package com.example.userservice.service;

import com.example.userservice.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    List<User> getAllUsers();
    User getUserById(UUID userId);
    User updateUser(UUID userId, User user);
    boolean deleteUser(UUID userId);
}
