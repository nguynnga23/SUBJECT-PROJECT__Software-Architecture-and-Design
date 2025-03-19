package com.example.userservice.config;

import com.example.userservice.service.JwtService;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.validation.annotation.Validated;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;

@Configuration
@Setter
@Getter
@ConfigurationProperties(prefix = "jwt")
@Validated
public class JwtConfig {

    private RSAPrivateKey privateKey;

    private RSAPublicKey publicKey;

    private Duration accessTokenTtl; // Thời gian sống của access token
    private Duration refreshTokenTtl; // Thời gian sống của refresh token

    @Bean
    public JwtEncoder jwtEncoder() {
        if (privateKey == null || publicKey == null) {
            throw new IllegalStateException("RSA keys must be configured");
        }
        final var jwk = new RSAKey.Builder(publicKey).privateKey(privateKey).build();

        return new NimbusJwtEncoder(new ImmutableJWKSet<>(new JWKSet(jwk)));
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        if (publicKey == null) {
            throw new IllegalStateException("Public key must be configured");
        }
        return NimbusJwtDecoder.withPublicKey(publicKey).build();
    }

    @Bean
    public JwtService jwtService(@Value("${spring.application.name}") final String appName, final JwtEncoder jwtEncoder,final JwtDecoder jwtDecoder) {
        if (accessTokenTtl == null || refreshTokenTtl == null) {
            throw new IllegalStateException("Access token TTL and refresh token TTL must be configured");
        }
        return new JwtService(appName, accessTokenTtl,refreshTokenTtl, jwtEncoder,jwtDecoder);
    }

}