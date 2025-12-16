package com.paypal.api_gateway.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.security.Key;


//    "token": "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiUk9MRV9VU0VSIiwidXNlcklkIjozLCJzdWIiOiJ2aXZla2Jhcmd1ZGhqZmUzQGdtYWlsLmNvbSIsImlhdCI6MTc2NTkwMzk5MSwiZXhwIjoxNzY1OTkwMzkxfQ.h9J3nOOjnkYrOWtnSYEbUfzEnBMfH6phJ2k-SoqzAA0"
public class JwtUtil {
    private static final String SECRET = "secret123secret123secret123secret123secret123secret123";

    private static Key getSigniningKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    public static Claims validateToken(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSigniningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}