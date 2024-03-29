package com.sourav.springsecurity.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
public class JWTUtil {

    @Value("${app.secret}")
    private static String secret;

    // Example secret key (keep this secret!)
    static String secretKey = secret;

    // Generate the token

    public String generateToken(String subject) {
        Map<String, Object> claims = new HashMap<>();
        return generateToken(claims, subject);
    }

    private String generateToken(Map<String, Object> claims, String subject) {
        String token = Jwts.builder()
                .subject(subject)
                .claim(claims)
                .issuer("demo")
                .issuedAt(LocalDateTime.now())
                .expiration(LocalDateTime.now().plusMinutes(30))
                .signWith(secretKey.getBytes())
                .compact();

        System.out.println(token);

        return token;
    }

    // Example secret key (keep this secret!)

    // Method to get claims from JWT token
    public static Claims getClaimsFromToken(String token) {
        return Jwts.parser().verifyWith().build().parse(token);
    }

    // Method to get username from claims
    public static String getUsernameFromClaims() {
        return getClaimsFromToken(secretKey).getSubject();
    }

}
