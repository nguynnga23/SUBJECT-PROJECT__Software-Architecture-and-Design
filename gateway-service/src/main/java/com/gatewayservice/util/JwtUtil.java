package com.gatewayservice.util;

import com.gatewayservice.exception.JwtTokenMalformedException;
import com.gatewayservice.exception.JwtTokenMissingException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;

import java.security.PublicKey;

public class JwtUtil {

    private final PublicKey publicKey;

    public JwtUtil(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public void validateToken(String token) throws JwtTokenMalformedException, JwtTokenMissingException {
        try {
            Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token);
        } catch (SignatureException e) {
            throw new JwtTokenMalformedException("Invalid JWT signature");
        } catch (Exception e) {
            throw new JwtTokenMissingException("JWT token validation failed: " + e.getMessage());
        }
    }

    public Claims getClaims(String token) {
        return Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token).getBody();
    }
}