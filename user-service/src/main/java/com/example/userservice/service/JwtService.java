package com.example.userservice.service;

import com.example.userservice.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Service
public class JwtService {

    private final String issuer;
    private final Duration accessTokenTtl ; // Thời gian sống của access token: 30 phút
    private final Duration refreshTokenTtl; // Thời gian sống của refresh token: 7 ngày
    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;
public JwtService(
        @Value("${jwt.issuer}") String issuer,
        @Value("${jwt.access-token-ttl}") Duration accessTokenTtl,
        @Value("${jwt.refresh-token-ttl}") Duration refreshTokenTtl,
        JwtEncoder jwtEncoder,
        JwtDecoder jwtDecoder

) {
    this.issuer = issuer;
    this.accessTokenTtl = accessTokenTtl;
    this.refreshTokenTtl = refreshTokenTtl;
    this.jwtEncoder = jwtEncoder;
    this.jwtDecoder = jwtDecoder;
    System.out.println("Access Token TTL: " + accessTokenTtl.getSeconds() + " seconds"); // Kiểm tra giá trị TTL
    Instant now = Instant.now();
    Instant expiry = now.plus(accessTokenTtl);

    System.out.println("Token created at: " + now);
    System.out.println("Token expires at: " + expiry);

}

    public String generateToken(User user) {
        final var claimsSet = JwtClaimsSet.builder()
                .subject(user.getUsername())
                .issuer(issuer)
                .expiresAt(Instant.now().plus(accessTokenTtl))
                .claim("role", user.getRole()) // Thêm role vào token
                .claim("id", user.getUserId())
                .claim("type", "access") // Phân biệt access token
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue();
    }
    public String generateRefreshToken(User user) {
        final var claimsSet = JwtClaimsSet.builder()
                .subject(user.getUsername())
                .issuer(issuer)
                .expiresAt(Instant.now().plus(accessTokenTtl))
                .claim("role", user.getRole()) // Thêm role vào token
                .claim("id", user.getUserId())
                .claim("type", "refresh") // Phân biệt access token
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue();
    }
    // Thêm phương thức extractUsername
    public String extractUsername(String token) {
        Jwt jwt = jwtDecoder.decode(token); // Giải mã token
        Instant expiry = jwt.getExpiresAt();
        Instant now = Instant.now();

        if (expiry != null && expiry.isBefore(now)) {
            throw new RuntimeException("Token đã hết hạn!");
        }
        return jwt.getSubject(); // Trích xuất username từ subject
    }

}