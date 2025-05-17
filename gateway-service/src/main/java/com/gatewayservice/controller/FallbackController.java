package com.gatewayservice.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class FallbackController {
    @RequestMapping("/fallback")
    public Mono<String> bookFallback() {
        return Mono.just("Service is currently unavailable. Please try again later.");
    }
}
