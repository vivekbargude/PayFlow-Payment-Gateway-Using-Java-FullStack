package com.paypal.user_service.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import io.jsonwebtoken.security.Keys;

@Component
public class JWTUtil {
    public static final String SECRET = "secret123secret123secret123secret123secret123secret123";

    public Key getSigningKey(){
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    public String extractEmail(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token, String username){
        try{
            extractEmail(token);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    //Considering email id as the username
    public String extractUsername(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String generateToken(Long userId, String email, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("role", role);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email) // Still keeping email as subject for backward compatibility
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 day
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractRole(String token) {
        return (String) Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role");
    }
}
