package com.example.userservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;

import java.util.Map;


public class ValidationException extends ErrorResponseException {

    public ValidationException(final HttpStatus status, final Map<String, String> errors) {
        super(status, ProblemDetailExt.forStatusDetailAndErrors(status, "Request validation failed", errors), null);
    }

}