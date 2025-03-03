package com.example.userservice.controller;


import com.example.userservice.dto.RegistrationRequestDto;
import com.example.userservice.dto.RegistrationResponseDto;
import com.example.userservice.mapper.UserRegistrationMapper;
import com.example.userservice.service.UserRegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class RegistrationController {

    private final UserRegistrationService userRegistrationService;

    private final UserRegistrationMapper userRegistrationMapper;

    @PostMapping("/register")
    public ResponseEntity<RegistrationResponseDto> registerUser(@Valid @RequestBody final RegistrationRequestDto registrationDTO) {
        final var registeredUser = userRegistrationService.registerUser(userRegistrationMapper.toEntity(registrationDTO));
        return ResponseEntity.ok(userRegistrationMapper.toRegistrationResponseDto(registeredUser));
    }

}