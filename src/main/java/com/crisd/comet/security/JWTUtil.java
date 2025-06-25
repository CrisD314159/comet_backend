package com.crisd.comet.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Component
public class JWTUtil {
    @Value("${jwt.key}")
    private String jwtKey;
    @Value("${jwt.refreshKey}")
    private String jwtRefreshKey;
    @Value("${jwt.tokenExpiration}")
    private int tokenExpiration;
    @Value("${jwt.refreshTokenExpiration}")
    private int refreshTokenExpiration;
    private SecretKey secretKey;
    private SecretKey refreshSecretKey;

    @PostConstruct
    public void init(){
        secretKey = Keys.hmacShaKeyFor(jwtKey.getBytes(StandardCharsets.UTF_8));
        refreshSecretKey = Keys.hmacShaKeyFor(jwtRefreshKey.getBytes(StandardCharsets.UTF_8));
    }

    public String GenerateToken(UUID id, String email, boolean refresh){
        long expirationInMs = refresh ? refreshTokenExpiration : tokenExpiration;
        return Jwts.builder()
                .setId(id.toString())
                .setSubject(email)
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(new Date((new Date().getTime() + expirationInMs)))
                .signWith(refresh ? secretKey : refreshSecretKey, SignatureAlgorithm.HS256)
                .compact();

    }

    public String GetEmailFromToken(String token, boolean refresh){
        SecretKey key = refresh ? refreshSecretKey : secretKey;
        return Jwts.parserBuilder()
                .setSigningKey(key).build()
                .parseClaimsJwt(token)
                .getBody()
                .getId();
    }
    public boolean ValidateJwtToken(String token, boolean refresh) {
        SecretKey key = refresh ? refreshSecretKey : secretKey;
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException e) {
            System.out.println("Invalid JWT signature: " + e.getMessage());
        } catch (MalformedJwtException e) {
            System.out.println("Invalid JWT token: " + e.getMessage());
        } catch (ExpiredJwtException e) {
            System.out.println("JWT token is expired: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.out.println("JWT token is unsupported: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("JWT claims string is empty: " + e.getMessage());
        }
        return false;
    }
}
