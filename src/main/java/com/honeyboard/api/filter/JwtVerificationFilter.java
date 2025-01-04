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
    private final List<String> excludedUrls = Arrays.asList(
            "/api/v1/auth",
            "/swagger-ui"
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

        String jwt = extractTokenFromCookies(request.getCookies(), ACCESS_TOKEN);

        if (jwt == null) {
            log.debug("액세스 토큰이 없음, 필터 체인 계속 진행");
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String userEmail = jwtService.extractUserEmail(jwt);
            log.debug("JWT에서 이메일 추출: {}", userEmail);

            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
                log.debug("사용자 상세 정보 로드 완료: {}", userEmail);

                if (jwtService.isValid(jwt, userDetails)) {
                    log.debug("JWT 토큰 유효성 검증 성공");
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    log.debug("인증 정보 설정 완료");
                }
            }
        } catch (ExpiredJwtException e) {
            log.debug("액세스 토큰 만료, 리프레시 토큰으로 갱신 시도");
            String refreshToken = extractTokenFromCookies(request.getCookies(), REFRESH_TOKEN);

            if (refreshToken != null) {
                try {
                    User user = ((CustomUserDetails) userDetailsService.loadUserByUsername(
                            jwtService.extractUserEmail(refreshToken))).getUser();
                    log.debug("리프레시 토큰으로 사용자 정보 조회 성공: {}", user.getEmail());

                    Map<String, String> tokens = jwtService.rotateTokens(refreshToken, user);

                    if (tokens != null) {
                        log.debug("새로운 토큰 발급 성공");
                        cookieUtil.addCookie(response, ACCESS_TOKEN, tokens.get("accessToken"),
                                (int) (jwtService.getAccessTokenExpire() / 1000));
                        cookieUtil.addCookie(response, REFRESH_TOKEN, tokens.get("refreshToken"),
                                (int) (jwtService.getRefreshTokenExpire() / 1000));

                        doFilterInternal(request, response, filterChain);
                        return;
                    }
                } catch (Exception ex) {
                    log.error("토큰 갱신 중 오류 발생: {}", ex.getMessage());
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }
            }
            log.debug("리프레시 토큰이 없거나 유효하지 않음");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        } catch (JwtException e) {
            log.error("JWT 처리 중 오류 발생: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        log.debug("JWT 검증 필터 완료");
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        boolean shouldNotFilter = excludedUrls.stream()
                .anyMatch(path::startsWith);
        log.debug("JWT 필터 제외 여부 확인 - URI: {}, 제외: {}", path, shouldNotFilter);
        return shouldNotFilter;
    }
}