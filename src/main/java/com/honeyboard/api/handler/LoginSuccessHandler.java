package com.honeyboard.api.handler;

import com.honeyboard.api.jwt.model.service.JwtService;
import com.honeyboard.api.user.model.CustomUserDetails;
import com.honeyboard.api.user.model.User;
import com.honeyboard.api.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final CookieUtil cookieUtil;
    @Value("${frontend.url}")
    private String frontendUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        log.debug("로그인 성공 핸들러 실행");
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();

        log.info("사용자 '{}' 로그인 성공", user.getName());

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        log.debug("JWT 토큰 생성 완료 - 액세스 토큰 길이: {}, 리프레시 토큰 길이: {}",
                accessToken.length(), refreshToken.length());

        cookieUtil.addCookie(response, "access_token", accessToken,
                (int) (jwtService.getAccessTokenExpire() / 1000));
        cookieUtil.addCookie(response, "refresh_token", refreshToken,
                (int) (jwtService.getRefreshTokenExpire() / 1000));
        log.debug("쿠키 설정 완료");
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");

    }
}
