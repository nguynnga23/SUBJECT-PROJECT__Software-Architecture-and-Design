package com.example.userservice.service;

import com.example.userservice.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAllUsers();
    User getUserById(long id);
    User createUser(User user);
    User updateUser(Long id, User user);
    User deleteUser(Long id);
}
