package com.honeyboard.api.handler;

import com.honeyboard.api.jwt.model.service.JwtService;
import com.honeyboard.api.user.model.CustomUserDetails;
import com.honeyboard.api.user.model.User;
import com.honeyboard.api.util.CookieUtil;
import jakarta.servlet.ServletException;
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
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final CookieUtil cookieUtil;
    @Value("${frontend.url}")
    private String frontendUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        log.debug("OAuth2 인증 성공 핸들러 시작");
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = customUserDetails.getUser();
        log.debug("인증된 사용자 정보: {}", user);

        // 신규 사용자 처리
        if (customUserDetails.getAttributes() != null
                && Boolean.TRUE.equals(customUserDetails.getAttributes().get("isNewUser"))) {
            log.info("신규 사용자 감지: {}", user.getEmail());
            String temporaryToken = jwtService.generateTemporaryToken(user.getEmail());
            log.debug("임시 토큰 생성 완료: {}", temporaryToken);

            cookieUtil.addCookie(response, "temporary_token", temporaryToken,
                    (int) (jwtService.getTemporaryTokenExpire() / 1000));
            log.debug("임시 토큰 쿠키 설정 완료");

            log.info("추가 정보 입력 페이지로 리다이렉트: {}", user.getEmail());
            response.sendRedirect(frontendUrl + "/oauth/google/additional");
            return;
        }

        // 기존 사용자 처리
        log.info("기존 사용자 로그인 처리: {}", user.getEmail());
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        log.debug("액세스 토큰 생성 완료: {}", accessToken);
        log.debug("리프레시 토큰 생성 완료: {}", refreshToken);

        cookieUtil.addCookie(response, "access_token", accessToken,
                (int) (jwtService.getAccessTokenExpire() / 1000));
        cookieUtil.addCookie(response, "refresh_token", refreshToken,
                (int) (jwtService.getRefreshTokenExpire() / 1000));
        log.debug("인증 토큰 쿠키 설정 완료");

        // 메인 페이지로 리다이렉트
        log.info("메인 페이지로 리다이렉트: {}", user.getEmail());
        response.sendRedirect(frontendUrl + "/login/callback");
    }

}
