package com.gatewayservice.util;

import com.gatewayservice.exception.JwtTokenMalformedException;
import com.gatewayservice.exception.JwtTokenMissingException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    private final String SECRET_KEY = "secret-key"; // Đảm bảo đây là key bảo mật của bạn

    public void validateToken(String token) throws JwtTokenMalformedException, JwtTokenMissingException {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
        } catch (SignatureException e) {
            throw new JwtTokenMalformedException("Invalid JWT token");
        } catch (Exception e) {
            throw new JwtTokenMissingException("JWT token is missing");
        }
    }

    public Claims getClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }
}
