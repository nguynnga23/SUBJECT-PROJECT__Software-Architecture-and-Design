package com.gatewayservice.filter;

import com.gatewayservice.exception.JwtTokenMalformedException;
import com.gatewayservice.exception.JwtTokenMissingException;
import com.gatewayservice.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticator {
    @Autowired
    private JwtUtil jwtUtil;

    public Claims authenticate(ServerHttpRequest request) throws JwtTokenMalformedException, JwtTokenMissingException {
        if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
            throw new JwtTokenMissingException("Missing Authorization header");
        }

        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new JwtTokenMalformedException("Invalid Authorization header");
        }

        String token = authHeader.substring(7);
        jwtUtil.validateToken(token);
        return jwtUtil.getClaims(token);
    }
}
