package com.honeyboard.api.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.honeyboard.api.jwt.model.service.JwtService;
import com.honeyboard.api.user.model.CustomUserDetails;
import com.honeyboard.api.user.model.User;
import com.honeyboard.api.util.CookieUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
	
	private final JwtService jwtService;
    private final CookieUtil cookieUtil;
    private final ObjectMapper objectMapper;
	
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, 
                                      HttpServletResponse response,
                                      Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();
        
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        
        cookieUtil.addCookie(response, "access_token", accessToken, 
                (int) (jwtService.getAccessTokenExpire() / 1000));
        
        cookieUtil.addCookie(response, "refresh_token", refreshToken, 
                (int) (jwtService.getRefreshTokenExpire() / 1000));
        
        ResponseEntity.status(HttpStatus.OK)
        		.contentType(MediaType.APPLICATION_JSON)
        		.body(null); // 여기에 유저 정보 담아야 함
        
    }
}
