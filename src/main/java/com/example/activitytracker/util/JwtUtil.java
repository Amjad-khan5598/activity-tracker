package com.example.activitytracker.util;

import com.example.activitytracker.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;

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
}
