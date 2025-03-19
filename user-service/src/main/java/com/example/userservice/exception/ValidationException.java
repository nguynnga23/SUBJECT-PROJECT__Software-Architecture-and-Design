package com.example.userservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;

import java.util.Map;

@Getter
public class ValidationException extends ErrorResponseException {
    private final HttpStatus status;
    private final Map<String, String> errors;
    private final Object requestBody; // Thêm trường để lưu body của request
    public ValidationException(final HttpStatus status, final Map<String, String> errors,Object requestBody) {
        super(status, ProblemDetailExt.forStatusDetailAndErrors(status, "Request validation failed", errors), null);
//        super("Validation failed");
        this.status = status;
        this.errors = errors;
        this.requestBody = requestBody; // Khởi tạo trường requestBody
    }
    
}