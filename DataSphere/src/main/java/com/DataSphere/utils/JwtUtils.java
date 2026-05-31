package com.DataSphere.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.DataSphere.entity.User;
import com.DataSphere.repo.UserRepo;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils {

    @Autowired
    private UserRepo userRepo;
    @Value("${jwt.secret}")
    private String secretKey;
    public ResponseEntity<?> login(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRole());
        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", generateToken(claims, user));
        tokens.put("refreshToken", generateRefreshToken(claims, user));
        return new ResponseEntity<>(tokens, HttpStatus.CREATED);
        
    }
    private String generateToken(Map<String, Object> claims, User user){
        claims.put("typ", "access");
        return Jwts.builder()
        .signWith(generateKey())
        .claims(claims)
        .subject(user.getUsername())
        .header().empty().add("typ","JWT")
        .and()
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 15))
        .compact();
    }
    private String generateRefreshToken(Map<String, Object> claims, User user){
        claims.put("typ", "refresh");
        return Jwts.builder()
        .signWith(generateKey())
        .claims(claims)
        .subject(user.getUsername())
        .header().empty().add("typ","JWT")
        .and()
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 15))
        .compact();
    }
    public SecretKey generateKey(){
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    // to extract claims
    public Claims extractClaims(String token){
        return Jwts.parser()
        .verifyWith(generateKey())
        .build()
        .parseSignedClaims(token)
        .getPayload();
    }
    public String getUsername(String token) {
        Claims claims = extractClaims(token);
        return claims.getSubject();
    }
    // to get roles
    public String extractRoles(String token){
        Claims claims = extractClaims(token);
        return claims.get("role", String.class);
    }
    public boolean validate(String token) {
        if (extractClaims(token).get("typ", String.class).equals("refresh")) {
            return false;
        }
        return extractClaims(token).getExpiration().after(new Date());
    }

    // to check if the refresh token is valid
    public boolean isValidRefreshToken(String refresh){
        if(extractClaims(refresh).getExpiration().after(new Date()) && !validate(refresh)){
            return true;
        }
        return false;
    }
    public String generateAccessToken(String refreshToken) {
       Map<String, Object> claims = new HashMap<>();
       claims.put("role", extractRoles(refreshToken));
       User user = userRepo.findByUsername(getUsername(refreshToken));
       return generateToken(claims, user);
    }
}
