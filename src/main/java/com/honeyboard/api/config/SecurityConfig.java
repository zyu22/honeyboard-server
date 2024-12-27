package com.honeyboard.api.config;

import com.honeyboard.api.auth.model.service.CustomOAuth2UserService;
import com.honeyboard.api.filter.JwtVerificationFilter;
import com.honeyboard.api.handler.LoginFailureHandler;
import com.honeyboard.api.handler.LoginSuccessHandler;
import com.honeyboard.api.handler.OAuth2AuthenticationFailureHandler;
import com.honeyboard.api.handler.OAuth2AuthenticationSuccessHandler;
import com.honeyboard.api.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtVerificationFilter jwtVerificationFilter;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
    private final UserDetailsService userDetailsService;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CookieUtil cookieUtil;
    private final LoginSuccessHandler loginSuccessHandler;
    private final LoginFailureHandler loginFailureHandler;
    private final LogoutHandler logoutHandler;
    @Value("${cors.allowed-origins}")
    private String[] allowOriginUrl;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers( // 여기 있는 경로는 인증 안 해도 됩니다
                                "/api/v1/**",
                                "/api/v1/auth/**",
                                "/oauth2/",
                                "/swagger-ui/**"
                        ).permitAll()
                        .requestMatchers("/api/v1/admin").hasRole("ADMIN") // admin 권한 확인
                        .requestMatchers("/api/v1/user").hasRole("USER") // user 권한 확인
                        .anyRequest() // 모든 요청에 대해
                        .authenticated() // 권한 확인 요청
                )
                .userDetailsService(userDetailsService) // 로그인 로직 실행
                .formLogin(form -> form  // 일반 로그인 설정 추가
                        .loginProcessingUrl("/api/v1/auth/login") // 이 경로에 대한 요청은 login
                        .successHandler(loginSuccessHandler) // 로그인 성공 시
                        .failureHandler(loginFailureHandler) // 로그인 실패 시
                )

                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService) // OAuth2 사용자 서비스 설정
                        )
                        .successHandler(oAuth2AuthenticationSuccessHandler) // 성공 시
                        .failureHandler(oAuth2AuthenticationFailureHandler) // 실패 시
                        .authorizationEndpoint(authorization -> authorization
                                .baseUri("/oauth2/authorization") // // OAuth2 인증 기본 URI
                        )
                )
                .addFilterBefore(jwtVerificationFilter, UsernamePasswordAuthenticationFilter.class) // JWT 필터 추가
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션 미사용
                )
                .exceptionHandling(
                        e -> e.accessDeniedHandler(
                                        (request, response, accessDeniedException) -> response.setStatus(403) // 접근 거부 시 403 응답
                                )
                                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))) // 인증 실패 시 401 응답
                .logout(l -> l
                        .logoutUrl("/api/v1/auth/logout") // 로그아웃 URL
                        .addLogoutHandler(logoutHandler) // 로그아웃 핸들러(아직 미구현)
                )
                .cors((corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() { // CORS 설정

                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

                        CorsConfiguration configuration = new CorsConfiguration(); // CORS 허용 출처 설정

                        configuration.setAllowedOrigins(Arrays.asList(allowOriginUrl)); // HTTP 메서드 허용 설정

                        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
                        configuration.setAllowCredentials(true); // 인증 정보 허용
                        configuration.addAllowedHeader("*"); // 모든 헤더 허용
                        configuration.setExposedHeaders(List.of("Authorization")); // Authorization 헤더 노출
                        configuration.setMaxAge(3600L);
                        return configuration;
                    }
                })));
        return http.build();
    }
}
