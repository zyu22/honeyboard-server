package com.honeyboard.api.filter;

import com.honeyboard.api.jwt.model.service.JwtService;
import com.honeyboard.api.user.model.CustomUserDetails;
import com.honeyboard.api.user.model.User;
import com.honeyboard.api.util.CookieUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtVerificationFilter extends OncePerRequestFilter {

    private static final String ACCESS_TOKEN = "access_token";
    private static final String REFRESH_TOKEN = "refresh_token";

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final CookieUtil cookieUtil;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    private final List<String> excludedUrls = Arrays.asList(
            "/api/v1/auth/**",
            "/favicon.ico",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/api/v1/user/reset-password"
    );

    private String extractTokenFromCookies(Cookie[] cookies, String tokenName) {
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (tokenName.equals(cookie.getName())) {
                    log.debug("쿠키에서 {} 토큰 추출 성공", tokenName);
                    return cookie.getValue();
                }
            }
        }
        log.debug("쿠키에서 {} 토큰을 찾을 수 없음", tokenName);
        return null;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        log.debug("JWT 검증 필터 시작 - URI: {}", request.getRequestURI());

        String accessToken = extractTokenFromCookies(request.getCookies(), ACCESS_TOKEN);
        String refreshToken = extractTokenFromCookies(request.getCookies(), REFRESH_TOKEN);

        try {
            if (accessToken != null) {
                try {
                    processAccessToken(accessToken, response);
                    filterChain.doFilter(request, response);
                    return;
                } catch (ExpiredJwtException e) {
                    log.debug("Access Token 만료, Refresh Token으로 갱신 시도");
                    handleExpiredToken(refreshToken, response);
                    return;
                }
            } else if (refreshToken != null) {
                handleExpiredToken(refreshToken, response);
                return;
            }
        } catch (JwtException e) {
            log.error("JWT 처리 중 오류 발생: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        log.debug("JWT 검증 필터 완료");
        filterChain.doFilter(request, response);
    }

    private void processAccessToken(String accessToken, HttpServletResponse response) {
        String userEmail = jwtService.extractUserEmail(accessToken);
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
            if (jwtService.isValid(accessToken, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
    }

    private void handleExpiredToken(String refreshToken, HttpServletResponse response) {
        if (refreshToken != null) {
            try {
                User user = ((CustomUserDetails) userDetailsService.loadUserByUsername(
                        jwtService.extractUserEmail(refreshToken))).getUser();

                Map<String, String> tokens = jwtService.rotateTokens(refreshToken, user);
                if (tokens != null) {
                    cookieUtil.addCookie(response, ACCESS_TOKEN, tokens.get("accessToken"),
                            (int) (jwtService.getAccessTokenExpire() / 1000));
                    cookieUtil.addCookie(response, REFRESH_TOKEN, tokens.get("refreshToken"),
                            (int) (jwtService.getRefreshTokenExpire() / 1000));

                    processAccessToken(tokens.get("accessToken"), response);
                    return;
                }
            } catch (Exception ex) {
                log.error("토큰 갱신 중 오류 발생: {}", ex.getMessage());
            }
        }
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        boolean shouldNotFilter = excludedUrls.stream()
                .anyMatch(pattern -> pathMatcher.match(pattern, path));
        log.debug("JWT 필터 제외 여부 확인 - URI: {}, 제외: {}", path, shouldNotFilter);
        return shouldNotFilter;
    }
}