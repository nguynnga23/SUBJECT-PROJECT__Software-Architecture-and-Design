package com.example.userservice.seed;

import com.example.userservice.entity.User;
import com.example.userservice.enums.Role;
import com.example.userservice.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserDataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDataSeeder(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            User admin = new User();
            admin.setUserId(UUID.randomUUID());
            admin.setUsername("admin");
            admin.setEmail("admin@example.com");
            admin.setPasswordHash(passwordEncoder.encode("admin123"));
            admin.setFullName("Administrator");
            admin.setRole(Role.ADMIN);
            userRepository.save(admin);

            User user = new User();
            user.setUserId(UUID.randomUUID());
            user.setUsername("user");
            user.setEmail("user@example.com");
            user.setPasswordHash(passwordEncoder.encode("user123"));
            user.setFullName("Regular User");
            user.setRole(Role.USER);
            userRepository.save(user);
        }
    }
}
