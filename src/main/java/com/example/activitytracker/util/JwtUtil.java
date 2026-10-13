package com.example.activitytracker.util;

import com.example.activitytracker.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    private static final int MIN_SECRET_BYTES = 32;

    private final SecretKey signingKey;
    private final long expirationMs;

    public JwtUtil(
            @Value("${jwt.secret:}") String secret,
            @Value("${jwt.expiration-ms:86400000}") long expirationMs) {

        if (secret == null || secret.isBlank()) {
            throw new IllegalStateException(
                    "JWT_SECRET is not configured");
        }

        byte[] keyBytes;

        try {
            keyBytes = Decoders.BASE64.decode(secret);
        } catch (RuntimeException ex) {
            throw new IllegalStateException(
                    "JWT_SECRET must be a valid Base64-encoded secret", ex);
        }

        if (keyBytes.length < MIN_SECRET_BYTES) {
            throw new IllegalStateException(
                    "JWT_SECRET must decode to at least 32 bytes");
        }

        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
        this.expirationMs = expirationMs;
    }

    public String generateToken(User user) {
        Date now = new Date();

        return Jwts.builder()
                .subject(user.getEmail())
                .claim("id", user.getId())
                .claim("role", user.getRole())
                .issuedAt(now)
                .expiration(new Date(now.getTime() + expirationMs))
                .signWith(signingKey)
                .compact();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractEmail(String token) {
        return extractAllClaims(token).getSubject();
    }

    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

    public boolean validateToken(String token) {
        try {
            extractAllClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }
}
