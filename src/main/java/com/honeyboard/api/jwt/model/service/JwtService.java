package com.honeyboard.api.jwt.model.service;

import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;

import com.honeyboard.api.user.model.User;

public interface JwtService {

	String extractUserEmail(String token);
	boolean isValid(String token, UserDetails user);
	boolean isValidRefreshToken(String token, User user);
	String generateAccessToken(User user);
	String generateRefreshToken(User user);
	String generateTemporaryToken(String email);
	long getAccessTokenExpire();
	long getRefreshTokenExpire();
	long getTemporaryTokenExpire();
	Map<String, String> rotateTokens(String oldRefreshToken, User user);
	void invalidateRefreshToken(String email);
	int getUserIdFromToken(String token);
	String getEmailFromToken(String token);
	String getRoleFromToken(String token);

}
