package com.example.userservice.service;

import com.example.userservice.dto.AuthenticationRequestDto;
import com.example.userservice.dto.AuthenticationResponseDto;
import com.example.userservice.entity.User;
import com.example.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public AuthenticationResponseDto authenticate(final AuthenticationRequestDto request) {
        final var authToken = UsernamePasswordAuthenticationToken.unauthenticated(request.username(), request.passwordHash());
        final var authentication = authenticationManager.authenticate(authToken);

        final var token = jwtService.generateToken(request.username());
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return new AuthenticationResponseDto(token,user.getEmail(), user.getFullName());
    }
}