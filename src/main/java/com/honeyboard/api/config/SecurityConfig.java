package com.honeyboard.api.config;

import com.honeyboard.api.auth.model.service.CustomOAuth2UserService;
import com.honeyboard.api.filter.JwtVerificationFilter;
import com.honeyboard.api.handler.LoginFailureHandler;
import com.honeyboard.api.handler.LoginSuccessHandler;
import com.honeyboard.api.handler.OAuth2AuthenticationFailureHandler;
import com.honeyboard.api.handler.OAuth2AuthenticationSuccessHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class SecurityConfig {

    private final JwtVerificationFilter jwtVerificationFilter;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
    private final UserDetailsService userDetailsService;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final LoginSuccessHandler loginSuccessHandler;
    private final LoginFailureHandler loginFailureHandler;
    private final LogoutHandler logoutHandler;

    @Value("${frontend.url}")
    private String frontendUrl;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.info("보안 필터 체인 구성 시작");

        http
                .csrf(csrf -> {
                    log.debug("CSRF 보호 비활성화");
                    csrf.disable();
                })
                .authorizeHttpRequests(auth -> {
                    log.debug("URL 기반 보안 설정 구성");
                    auth.requestMatchers(
                                    "/api/v1/**",
                                    "/api/v1/auth/**",
                                    "/oauth2/**",
                                    "/login/oauth2/**",
                                    "/swagger-ui/**",
                                    "/api/v1/auth/logout"
                            ).permitAll()
                            .requestMatchers("/api/v1/admin").hasRole("ADMIN")
                            .requestMatchers("/api/v1/user").hasRole("USER")
                            .anyRequest()
                            .authenticated();
                    log.debug("URL 보안 설정 완료");
                })
                .userDetailsService(userDetailsService)
                .formLogin(form -> {
                    log.debug("폼 로그인 설정 구성");
                    form.loginProcessingUrl("/api/v1/auth/login")
                            .successHandler(loginSuccessHandler)
                            .failureHandler(loginFailureHandler);
                    log.debug("폼 로그인 설정 완료");
                })
                .oauth2Login(oauth2 -> {
                    log.debug("OAuth2 로그인 설정 구성");
                    oauth2.userInfoEndpoint(userInfo -> {
                                log.debug("OAuth2 사용자 서비스 설정");
                                userInfo.userService(customOAuth2UserService);
                            })
                            .successHandler(oAuth2AuthenticationSuccessHandler)
                            .failureHandler(oAuth2AuthenticationFailureHandler)
                            .authorizationEndpoint(authorization -> {
                                log.debug("OAuth2 인증 기본 URI 설정: /oauth2/authorization");
                                authorization.baseUri("/oauth2/authorization");
                            });
                    log.debug("OAuth2 로그인 설정 완료");
                })
                .addFilterBefore(jwtVerificationFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session -> {
                    log.debug("세션 관리 정책 설정: STATELESS");
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .exceptionHandling(e -> {
                    log.debug("보안 예외 처리 구성");
                    e.accessDeniedHandler((request, response, accessDeniedException) -> {
                                log.warn("접근 거부 처리: {}", accessDeniedException.getMessage());
                                response.setStatus(403);
                            })
                            .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
                })
                .logout(l -> l
                        .logoutUrl("/api/v1/auth/logout")
                        .addLogoutHandler(logoutHandler)
                        .logoutSuccessHandler((request, response, authentication) -> {
                            response.setStatus(HttpServletResponse.SC_OK);
                        })  // 리다이렉트 방지
                        .permitAll()
                )
                .cors(corsCustomizer -> {
                    log.debug("CORS 설정 구성");
                    corsCustomizer.configurationSource(new CorsConfigurationSource() {
                        @Override
                        public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                            log.debug("CORS 설정 생성 - 요청 URI: {}", request.getRequestURI());

                            CorsConfiguration configuration = new CorsConfiguration();
                            configuration.setAllowedOrigins(List.of(frontendUrl));
                            configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
                            configuration.setAllowCredentials(true);
                            configuration.addAllowedHeader("*");
                            configuration.setExposedHeaders(Arrays.asList(
                                    "Authorization",
                                    "Set-Cookie",
                                    "Access-Control-Allow-Credentials",
                                    "Access-Control-Allow-Origin"
                            ));
                            configuration.setAllowedHeaders(Arrays.asList(
                                    "Authorization",
                                    "Content-Type",
                                    "Cookie",
                                    "Access-Control-Allow-Credentials",
                                    "Access-Control-Allow-Origin"
                            ));
                            configuration.setMaxAge(3600L);

                            log.debug("CORS 허용 출처: {}", frontendUrl);
                            log.debug("CORS 허용 메서드: {}", configuration.getAllowedMethods());
                            log.debug("CORS 노출 헤더: {}", configuration.getExposedHeaders());

                            return configuration;
                        }
                    });
                    log.debug("CORS 설정 완료");
                });

        log.info("보안 필터 체인 구성 완료");
        return http.build();
    }
}