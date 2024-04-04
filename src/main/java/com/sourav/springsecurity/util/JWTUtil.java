package com.sourav.springsecurity.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.security.auth.kerberos.EncryptionKey;
import java.security.PublicKey;
import java.security.interfaces.DSAPublicKey;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class JWTUtil {

    @Value("${app.secret}")
    private static String secret;

    // Example secret key (keep this secret!)
    static String secretKey = secret;

    static SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes());

    // Generate the token

    public static String generateToken(String subject) {
        Map<String, Object> claims = new HashMap<>();
        return generateToken(claims, subject);
    }

    private static String generateToken(Map<String, Object> claims, String subject) {
        String token = Jwts.builder()
                .subject(subject)
                .claims(claims)
                .issuer("https://issuer.example.com")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMinutes(2)))
                .signWith(key)
                .compact();

        System.out.println(token);

        return token;
    }

    // Example secret key (keep this secret!)

    // Method to get claims from JWT token
    public static Claims getClaimsFromToken(String token) {
        return (Claims) Jwts
                .parser()
                .requireIssuer("https://issuer.example.com")
                .verifyWith(key)
                .build()
                .parseSignedClaims(token);
    }

    // Method to get username from claims
    public String getUsernameFromClaims(String token) {
        return getClaimsFromToken(token).getSubject();
    }

}
