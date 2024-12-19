package com.honeyboard.api.util;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletResponse;

@Component
public class CookieUtil {
    public void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        ResponseCookie cookie = ResponseCookie.from(name, value)
                .path("/") // 쿠키의 사용 경로 설정 (전체 도메인)
                .sameSite("Lax") // CSRF 공격 방지를 위한 SameSite 설정
                .httpOnly(true) // JavaScript를 통한 접근 방지
                .secure(true) // HTTPS에서만 전송되도록 설정, 아마 로컬에서 돌릴거면 이거 false 해놔야 할 것 같습니다
                .maxAge(maxAge) // 쿠키의 유효 기간 설정
                .build();
                
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    public void deleteCookie(HttpServletResponse response, String name) {
        ResponseCookie cookie = ResponseCookie.from(name, "") // 빈 값으로 설정
                .path("/")
                .sameSite("Lax")
                .httpOnly(true)
                .secure(true) // HTTPS에서만 전송되도록 설정, 아마 로컬에서 돌릴거면 이거 false 해놔야 할 것 같습니다
                .maxAge(0) // 즉시 만료되도록 설정
                .build();
                
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}
