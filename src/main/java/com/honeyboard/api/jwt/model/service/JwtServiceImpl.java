package com.honeyboard.api.jwt.model.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
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
    
    @Value("${jwt.temporary-token-expiration}")
    private long temporaryTokenExpire;

    private final RedisTemplate<String, String> redisTemplate;
    private static final String REFRESH_TOKEN_PREFIX = "RT:";
    
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
        if (!userEmail.equals(user.getEmail()) || isTokenExpired(token)) {
            return false;
        }

        String storedToken = redisTemplate.opsForValue().get(REFRESH_TOKEN_PREFIX + user.getEmail());
        return token.equals(storedToken);
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
                .verifyWith(getSigninKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }


    public String generateAccessToken(User user) {
        return generateToken(user, accessTokenExpire);
    }

    public String generateRefreshToken(User user) {
        String refreshToken = generateToken(user, refreshTokenExpire );
        saveRefreshToken(user.getEmail(), refreshToken);
        return refreshToken;
    }
    
    public String generateTemporaryToken(String email) {
    	String temporaryToken = generateTempToken(email, temporaryTokenExpire);
    	return temporaryToken;
    }
    
    private String generateTempToken(String email, long expireTime) {
    	return Jwts
                .builder()
                .subject(email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expireTime))
                .signWith(getSigninKey())
                .compact();
    }

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
    
    private void saveRefreshToken(String email, String refreshToken) {
        redisTemplate.opsForValue().set(
            REFRESH_TOKEN_PREFIX + email,
            refreshToken,
            refreshTokenExpire,
            TimeUnit.MILLISECONDS
        );
    }

    private SecretKey getSigninKey() {
        byte[] keyBytes = Decoders.BASE64URL.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Map<String, String> rotateTokens(String oldRefreshToken, User user) {
        if (!isValidRefreshToken(oldRefreshToken, user)) {
            invalidateRefreshToken(user.getEmail());
            return null;
        }

        // 새로운 토큰 쌍 생성
        String newAccessToken = generateAccessToken(user);
        String newRefreshToken = generateRefreshToken(user);
        Map tokens = new HashMap<>();
        tokens.put("accessToken", newAccessToken);
        tokens.put("refreshToken", newRefreshToken);

        return tokens;
    }

    public void invalidateRefreshToken(String email) {
        redisTemplate.delete(REFRESH_TOKEN_PREFIX + email);
    }
    
    public long getAccessTokenExpire() {
    	return accessTokenExpire;
    }
    
    public long getRefreshTokenExpire() {
        return refreshTokenExpire;
    }
    
    public long getTemporaryTokenExpire() {
    	return temporaryTokenExpire;
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
