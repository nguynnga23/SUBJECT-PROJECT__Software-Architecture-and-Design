package com.example.userservice.controller;


import com.example.userservice.dto.RegistrationRequestDto;
import com.example.userservice.dto.RegistrationResponseDto;
import com.example.userservice.entity.User;
import com.example.userservice.mapper.UserRegistrationMapper;
import com.example.userservice.service.UserRegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/users")
public class RegistrationController {


    private static final Logger logger = LoggerFactory.getLogger(RegistrationController.class);

    private final UserRegistrationService userRegistrationService;
    private final UserRegistrationMapper userRegistrationMapper;

    public RegistrationController(UserRegistrationService userRegistrationService, UserRegistrationMapper userRegistrationMapper) {
        this.userRegistrationService = userRegistrationService;
        this.userRegistrationMapper = userRegistrationMapper;
    }

@PostMapping("/register")
public ResponseEntity<RegistrationResponseDto> registerUser(@RequestBody RegistrationRequestDto registrationRequestDto) {
    logger.info("Received registration request: {}", registrationRequestDto);
    User user = userRegistrationService.registerUser(registrationRequestDto);
    RegistrationResponseDto response = new RegistrationResponseDto(user.getUsername(), user.getEmail(), user.getFullName(), user.getPasswordHash());
    return ResponseEntity.ok(response);
}

}