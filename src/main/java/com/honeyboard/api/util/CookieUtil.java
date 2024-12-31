package com.honeyboard.api.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CookieUtil {

    @Value("${cookie.secure-flag:false}")
    private boolean secureCookie;

    public void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        log.debug("쿠키 생성 시작 - 이름: {}, 만료시간: {}초", name, maxAge);

        ResponseCookie cookie = ResponseCookie.from(name, value)
                .path("/")
                .sameSite("Lax")
                .httpOnly(true)
                .secure(secureCookie)     // application.properties에서 설정한 값 사용
                .maxAge(maxAge)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        log.debug("쿠키 생성 완료 - 설정: path=/, sameSite=Lax, httpOnly=true, secure={}", secureCookie);
    }

    public void deleteCookie(HttpServletResponse response, String name) {
        log.debug("쿠키 삭제 시작 - 이름: {}", name);

        ResponseCookie cookie = ResponseCookie.from(name, "")
                .path("/")
                .sameSite("Lax")
                .httpOnly(true)
                .secure(secureCookie)
                .maxAge(0)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        log.debug("쿠키 삭제 완료 - 이름: {}", name);
    }

    public
    Cookie getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return cookie;
                }
            }
        }

        return null;
    }
}