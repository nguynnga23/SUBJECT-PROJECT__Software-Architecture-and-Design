package com.example.userservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(ValidationException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", ex.getStatus().value());
        response.put("message", ex.getMessage());
        response.put("errors", ex.getErrors());
        response.put("requestBody", ex.getRequestBody()); // Thêm body của request vào response

        return new ResponseEntity<>(response, ex.getStatus());
    }
}