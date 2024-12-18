package com.honeyboard.api.jwt.model.service;

import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.honeyboard.api.user.model.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.access-token-expiration}")
    private long accessTokenExpire;

    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpire;

    @Override
    public String extractUserEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public boolean isValid(String token, UserDetails user) {
        String userEmail = extractUserEmail(token);
        return (userEmail.equals(user.getUsername())) && !isTokenExpired(token);
    }

    public boolean isValidRefreshToken(String token, User user) {
        String userEmail = extractUserEmail(token);
        return (userEmail.equals(user.getEmail())) && !isTokenExpired(token);
    }


    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
//                .verifyWith(getSigninKey())
                .setSigningKey(getSigninKey())
                .build()
                .parseSignedClaims(token)
//                .getPayload();
                .getBody();
    }


    public String generateAccessToken(User user) {
        return generateToken(user, accessTokenExpire);
    }

    public String generateRefreshToken(User user) {
        return generateToken(user, refreshTokenExpire );
    }

    //86400000 밀리세컨드-> 86400세컨드 -> 1440 분 -> 24시간
    private String generateToken(User user, long expireTime) {
        return Jwts
                .builder()
                .subject(user.getEmail())
                .claim("userId", user.getUserId())
                .claim("role", user.getRole())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expireTime))
                .signWith(getSigninKey())
                .compact();
    }

    private SecretKey getSigninKey() {
        byte[] keyBytes = Decoders.BASE64URL.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public int getUserIdFromToken(String token) {
        return extractAllClaims(token).get("userId", Integer.class);
    }

    public String getEmailFromToken(String token) {
        return extractAllClaims(token).getSubject(); // Subject 값
    }

    public String getRoleFromToken(String token) {
        return extractAllClaims(token).get("role", String.class);
    }
}
