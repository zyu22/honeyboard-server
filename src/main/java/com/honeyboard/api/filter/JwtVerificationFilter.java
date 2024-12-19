package com.honeyboard.api.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

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

@Component
@RequiredArgsConstructor
public class JwtVerificationFilter extends OncePerRequestFilter {
    
    private static final String ACCESS_TOKEN = "access_token";
    private static final String REFRESH_TOKEN = "refresh_token";
    
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final CookieUtil cookieUtil;

    private String extractTokenFromCookies(Cookie[] cookies, String tokenName) {
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (tokenName.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String jwt = extractTokenFromCookies(request.getCookies(), ACCESS_TOKEN);
        
        if (jwt == null) {
            filterChain.doFilter(request, response);
            return;
        }
        
        try {
            String userEmail = jwtService.extractUserEmail(jwt);
            
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
                
                if (jwtService.isValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                    );
                    
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (ExpiredJwtException e) {
            String refreshToken = extractTokenFromCookies(request.getCookies(), REFRESH_TOKEN);
            
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
                            
                        doFilterInternal(request, response, filterChain);
                        return;
                    }
                } catch (Exception ex) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }
            }
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        } catch (JwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        
        filterChain.doFilter(request, response);
    }

    private final List<String> excludedUrls = Arrays.asList(
        "/api/v1/auth",
        "/swagger-ui",
        "/api/v1"
    );

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return excludedUrls.stream()
            .anyMatch(path::startsWith);
    }
}
