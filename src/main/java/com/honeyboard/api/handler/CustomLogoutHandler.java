package com.honeyboard.api.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import com.honeyboard.api.jwt.model.service.JwtService;
import com.honeyboard.api.util.CookieUtil;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor

public class CustomLogoutHandler implements LogoutHandler {
    
    private static final String ACCESS_TOKEN = "access_token";
    private static final String REFRESH_TOKEN = "refresh_token";
    
    private final JwtService jwtService;
    private final CookieUtil cookieUtil;

    @Override
    public void logout(HttpServletRequest request, 
                      HttpServletResponse response, 
                      Authentication authentication) {
        try {
            // 쿠키에서 리프레시 토큰 추출
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (REFRESH_TOKEN.equals(cookie.getName())) {
                        // Redis에서 리프레시 토큰 삭제
                        String userEmail = jwtService.extractUserEmail(cookie.getValue());
                        jwtService.invalidateRefreshToken(userEmail);
                        break;
                    }
                }
            }

            // 쿠키 삭제
            cookieUtil.deleteCookie(response, ACCESS_TOKEN);
            cookieUtil.deleteCookie(response, REFRESH_TOKEN);

            // SecurityContext 초기화
            SecurityContextHolder.clearContext();
            
            // 로그아웃 성공 응답
            response.setStatus(HttpServletResponse.SC_OK);
            
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
