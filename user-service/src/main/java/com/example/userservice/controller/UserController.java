package com.example.userservice.controller;

import com.example.userservice.entity.User;
import com.example.userservice.service.UserRedisService;
import com.example.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/profile/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable UUID userId) {
        try {
            User user = userService.getUserById(userId);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }

    }

    // As a registry --- Nguyen Chung must change createUser
    @PostMapping()
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User newUser = userService.createUser(user);
        return ResponseEntity.ok(newUser);
    }

    @PutMapping("/profile/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable UUID userId, @RequestBody User user) {
        return ResponseEntity.ok(userService.updateUser(userId, user));
    }



    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable UUID userId) {
        boolean isDeleted = userService.deleteUser(userId);

        if (isDeleted) {
            return ResponseEntity.ok(Map.of("message", "User deleted successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "User not found"));
        }
    }
}
