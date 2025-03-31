package com.gatewayservice.filter;

import com.gatewayservice.exception.JwtTokenMalformedException;
import com.gatewayservice.exception.JwtTokenMissingException;
import com.gatewayservice.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Predicate;

@Component
public class JwtAuthenticationFilter implements GatewayFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        System.out.println("Request path: " + path); // Debug

        // Danh sách endpoint công khai
        final List<String> apiEndpoints = List.of(
                "/api/v1/user-service/users/login",
                "/api/v1/user-service/users/register"
        );

        // Kiểm tra xem request có cần bảo mật không
        Predicate<ServerHttpRequest> isApiSecured = r -> apiEndpoints.stream()
                .noneMatch(uri -> r.getURI().getPath().equals(uri));

        if (isApiSecured.test(request)) {
            if (!request.getHeaders().containsKey("Authorization")) {
                System.out.println("Missing Authorization header for path: " + path);
                ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.setComplete();
            }

            // Lấy token từ header Authorization
            final String authHeader = request.getHeaders().getFirst("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                System.out.println("Invalid Authorization header: " + authHeader);
                ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.setComplete();
            }

            final String token = authHeader.substring(7); // Bỏ "Bearer "

            try {
                jwtUtil.validateToken(token);
            } catch (JwtTokenMalformedException | JwtTokenMissingException e) {
                System.out.println("Token validation failed: " + e.getMessage());
                ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.BAD_REQUEST);
                return response.setComplete();
            }

            // Thêm thông tin từ claims vào header
            Claims claims = jwtUtil.getClaims(token);
            request = exchange.getRequest().mutate()
                    .header("id", String.valueOf(claims.get("id")))
                    .build();
        } else {
            System.out.println("Public endpoint, skipping authentication: " + path);
        }

        return chain.filter(exchange.mutate().request(request).build());
    }
}