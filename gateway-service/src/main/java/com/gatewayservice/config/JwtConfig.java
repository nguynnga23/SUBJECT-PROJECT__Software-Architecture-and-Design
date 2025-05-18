package com.gatewayservice.config;

import com.gatewayservice.util.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.stream.Collectors;

@Configuration
public class JwtConfig {
    @Bean
    public JwtUtil jwtUtil() throws Exception {
        // Đọc file từ classpath
        ClassPathResource resource = new ClassPathResource("publickey.pem");
        String publicKeyPem;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            publicKeyPem = reader.lines().collect(Collectors.joining("\n"));
        }
//        String publicKeyPem = new String(Files.readAllBytes(resource.getFile().toPath()));
        String key = publicKeyPem.replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");
        byte[] keyBytes = Base64.getDecoder().decode(key);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PublicKey publicKey = kf.generatePublic(spec);
        return new JwtUtil(publicKey);
    }
}