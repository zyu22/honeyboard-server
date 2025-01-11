package com.honeyboard.api.jwt.model.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import javax.crypto.SecretKey;

import com.honeyboard.api.exception.*;
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

    @Value("${jwt.access-token-expiration}") // 액세스 토큰 만료시간
    private long accessTokenExpire;

    @Value("${jwt.refresh-token-expiration}") // 리프레시 토큰 만료시간
    private long refreshTokenExpire;
    
    @Value("${jwt.temporary-token-expiration}") // 임시 토큰 만료시간
    private long temporaryTokenExpire;

    private final RedisTemplate<String, String> redisTemplate; // 레디스 연동
    private static final String REFRESH_TOKEN_PREFIX = "RT:"; // // Redis에 저장될 리프레시 토큰의 접두어

    @Override
    public String extractUserEmail(String token) {
        try {
            return extractClaim(token, Claims::getSubject);
        } catch (Exception e) {
            throw new BusinessException(
                    ErrorCode.INVALID_TOKEN,
                    "유효하지 않은 토큰입니다."
            );
        }
    }

    @Override
    public boolean isValid(String token, UserDetails user) {
        try {
            String userEmail = extractUserEmail(token);
            if (!userEmail.equals(user.getUsername())) {
                throw new BusinessException(
                        ErrorCode.INVALID_TOKEN,
                        "토큰의 사용자 정보가 일치하지 않습니다."
                );
            }
            if (isTokenExpired(token)) {
                throw new BusinessException(
                        ErrorCode.TOKEN_EXPIRED
                );
            }
            return true;
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(
                    ErrorCode.INVALID_TOKEN,
                    "토큰 검증 중 오류가 발생했습니다."
            );
        }
    }

    @Override
    public boolean isValidRefreshToken(String token, User user) {
        try {
            String userEmail = extractUserEmail(token);
            if (!userEmail.equals(user.getEmail())) {
                throw new BusinessException(
                        ErrorCode.INVALID_TOKEN,
                        "리프레시 토큰의 사용자 정보가 일치하지 않습니다."
                );
            }
            if (isTokenExpired(token)) {
                throw new BusinessException(
                        ErrorCode.TOKEN_EXPIRED,
                        "만료된 리프레시 토큰입니다."
                );
            }

            String storedToken = redisTemplate.opsForValue().get(REFRESH_TOKEN_PREFIX + user.getEmail());
            if (storedToken == null) {
                throw new BusinessException(
                        ErrorCode.REFRESH_TOKEN_NOT_FOUND,
                        "저장된 리프레시 토큰을 찾을 수 없습니다."
                );
            }
            if (!token.equals(storedToken)) {
                throw new BusinessException(
                        ErrorCode.INVALID_TOKEN,
                        "유효하지 않은 리프레시 토큰입니다."
                );
            }
            return true;
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(
                    ErrorCode.INVALID_TOKEN,
                    "리프레시 토큰 검증 중 오류가 발생했습니다."
            );
        }
    }


    private boolean isTokenExpired(String token) { // 토큰 만료 여부 확인
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) { // 토큰의 만료 시간 추출
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> resolver) { // 토큰에서 특정 클레임 추출
        Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    private Claims extractAllClaims(String token) { // 토큰에서 모든 클레임 추출
        return Jwts
                .parser()
                .verifyWith(getSigninKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }


    public String generateAccessToken(User user) { // 액세스 토큰 생성
        return generateToken(user, accessTokenExpire);
    }

    public String generateRefreshToken(User user) { // 리프레시 토큰 생성 및 Redis에 저장
        String refreshToken = generateToken(user, refreshTokenExpire );
        saveRefreshToken(user.getEmail(), refreshToken);
        return refreshToken;
    }
    
    public String generateTemporaryToken(String email) { // 임시 토큰 생성 (이메일 인증 등에 사용)
    	String temporaryToken = generateTempToken(email, temporaryTokenExpire);
    	return temporaryToken;
    }
    
    private String generateTempToken(String email, long expireTime) { // 임시 토큰 생성 로직
    	return Jwts
                .builder()
                .subject(email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expireTime))
                .signWith(getSigninKey())
                .compact();
    }

    private String generateToken(User user, long expireTime) { // JWT 토큰 생성 (사용자 정보와 만료 시간 포함)
        return Jwts
                .builder()
                .subject(user.getEmail())
                .claim("userId", user.getUserId()) // 토큰에 id 저장
                .claim("role", user.getRole()) // 토큰에 role 저장
                .claim("generationId", user.getGenerationId())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expireTime))
                .signWith(getSigninKey())
                .compact();
    }
    
    private void saveRefreshToken(String email, String refreshToken) { // 리프레시 토큰을 Redis에 저장
        redisTemplate.opsForValue().set(
            REFRESH_TOKEN_PREFIX + email,
            refreshToken,
            refreshTokenExpire,
            TimeUnit.MILLISECONDS
        );
    }

    private SecretKey getSigninKey() { // JWT 서명에 사용할 키 생성
        byte[] keyBytes = Decoders.BASE64URL.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Map<String, String> rotateTokens(String oldRefreshToken, User user) {
        try {
            if (!isValidRefreshToken(oldRefreshToken, user)) {
                throw new BusinessException(
                        ErrorCode.INVALID_TOKEN,
                        "유효하지 않은 리프레시 토큰입니다."
                );
            }

            String newAccessToken = generateAccessToken(user);
            String newRefreshToken = generateRefreshToken(user);

            Map<String, String> tokens = new HashMap<>();
            tokens.put("accessToken", newAccessToken);
            tokens.put("refreshToken", newRefreshToken);

            return tokens;
        } catch (Exception e) {
            invalidateRefreshToken(user.getEmail());
            throw e;
        }
    }

    public void invalidateRefreshToken(String email) { // Redis에서 리프레시 토큰 삭제
        redisTemplate.delete(REFRESH_TOKEN_PREFIX + email);
    }
    
    // 각종 토큰의 만료 시간을 반환하는 getter 메서드들
    public long getAccessTokenExpire() {
    	return accessTokenExpire;
    }
    
    public long getRefreshTokenExpire() {
        return refreshTokenExpire;
    }
    
    public long getTemporaryTokenExpire() {
    	return temporaryTokenExpire;
    }

    // 토큰에서 각종 정보를 추출하는 메서드들
    public int getUserIdFromToken(String token) {
        return extractAllClaims(token).get("userId", Integer.class);
    }

    public String getEmailFromToken(String token) {
        return extractAllClaims(token).getSubject();
    }

    public String getRoleFromToken(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

    @Override
    public User getUserInfoFromToken(String token) {
        try {
            Claims claims = extractAllClaims(token);
            return User.builder()
                    .userId(claims.get("userId", Integer.class))
                    .email(claims.getSubject())
                    .role(claims.get("role", String.class))
                    .generationId(claims.get("generationId", Integer.class))
                    .build();
        } catch (Exception e) {
            throw new BusinessException(
                    ErrorCode.INVALID_TOKEN,
                    "토큰에서 사용자 정보를 추출할 수 없습니다."
            );
        }
    }
}
