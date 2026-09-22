package com.example.activitytracker.util;

import com.example.activitytracker.model.User;
import io.jsonwebtoken.Claims;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
	private final String SECRET_KEY = System.getenv("JWT_SECRET");
	
	
	private Key getSigningKey() {
	    byte[] keyBytes = SECRET_KEY.getBytes();
	    return Keys.hmacShaKeyFor(keyBytes);
	}
	public String generateToken(User user) {
		return Jwts.builder()
	            .subject(user.getEmail())
	            .claim("id", user.getId())
	            .claim("role", user.getRole())
	            .issuedAt(new Date())
	            .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
	            .signWith(getSigningKey())
	            .compact();
	}
	private Claims extractAllClaims(String token) {
	    return Jwts.parser()
	            .verifyWith((SecretKey) getSigningKey())
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
	    } catch (Exception e) {
	        return false;
	    }
	}
}
