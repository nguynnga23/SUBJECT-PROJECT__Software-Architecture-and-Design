package com.example.userservice.service.impl;

import com.example.userservice.entity.User;
import com.example.userservice.enums.Role;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRedisServiceImpl userRedisServiceImpl;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, UserRedisServiceImpl userRedisServiceImpl) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRedisServiceImpl = userRedisServiceImpl;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    @Override
    public User getUserById(UUID userId) {
        String cacheKey = "USER_" + userId;
        // Check cache
        User cachedUser = (User) userRedisServiceImpl.get(cacheKey);
        if(cachedUser != null) {
            return cachedUser;
        }
        // If any, find database
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }


    @Override
    public User createUser(User user) {
        if (user.getRole() == null) {
            user.setRole(Role.USER); // define USER
        }
        if (user.getCreatedAt() == null) {
            user.setCreatedAt(Instant.now().atZone(ZoneId.systemDefault()).toLocalDate());
        }
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        User newUser = userRepository.save(user);
        userRedisServiceImpl.save("USER_" + newUser.getUserId(), newUser, 10);
        return newUser;
    }

    @Override
    public User updateUser(UUID userId, User user) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found");
        }
        user.setUserId(userId);
        User updatedUser = userRepository.save(user);
        userRedisServiceImpl.save("USER_" + userId, updatedUser, 10);
        return updatedUser;
    }

    @Override
    public boolean deleteUser(UUID userId) {
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);

            // delete Cache
            userRedisServiceImpl.delete("USER_" + userId);
            return true;
        }
        return false;
    }
}
