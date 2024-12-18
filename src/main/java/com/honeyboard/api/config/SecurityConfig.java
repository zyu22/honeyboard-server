package com.honeyboard.api.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.honeyboard.api.filter.JwtAuthenticationFilter;
import com.honeyboard.api.handler.LoginFailureHandler;
import com.honeyboard.api.handler.LoginSuccessHandler;
import com.honeyboard.api.handler.OAuth2AuthenticationFailureHandler;
import com.honeyboard.api.handler.OAuth2AuthenticationSuccessHandler;
import com.honeyboard.api.user.model.service.CustomOAuth2UserService;
import com.honeyboard.api.util.CookieUtil;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	
	private final String[] allowOriginUrl;
	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
	private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
	private final UserDetailsService userDetailsServiceImpl;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CookieUtil cookieUtil;
    private final LoginSuccessHandler loginSuccessHandler;
    private final LoginFailureHandler loginFailureHandler;
    private final LogoutHandler logoutHandler;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(auth -> auth
						.requestMatchers(
								"/api/v1/**",
								"/api/v1/auth/**",
								"/swagger-ui/**"
								).permitAll()
						.requestMatchers("api/v1/admin").hasRole("ADMIN")
						.requestMatchers("api/v1/user").hasRole("USER")
						.anyRequest()
						.authenticated()
				)
				.formLogin(form -> form  // 일반 로그인 설정 추가
                        .loginProcessingUrl("/api/v1/auth/login")
                        .successHandler(loginSuccessHandler)
                        .failureHandler(loginFailureHandler)
                )
				.userDetailsService(userDetailsServiceImpl)
				.oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService)
                        )
                        .successHandler(oAuth2AuthenticationSuccessHandler)
                        .failureHandler(oAuth2AuthenticationFailureHandler)
                        .authorizationEndpoint(authorization -> authorization
                        .baseUri("/api/v1/auth/{domainName}/authorization")
                        )
                )
				.addFilterBefore(new JwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
				.sessionManagement(session -> session
						.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				)
				.exceptionHandling(
                        e -> e.accessDeniedHandler(
                                        (request, response, accessDeniedException) -> response.setStatus(403)
                                )
                                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
				.logout(l -> l
                        .logoutUrl("/api/v1/user/logout")
                        .addLogoutHandler(logoutHandler)
                        .logoutSuccessHandler((request, response, authentication) -> {
                            SecurityContextHolder.clearContext();
                            cookieUtil.deleteCookie(response, "jwt");
                        })
                )
				.cors((corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {

                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

                        CorsConfiguration configuration = new CorsConfiguration();

                        configuration.setAllowedOrigins(Arrays.asList(allowOriginUrl));

                        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
                        configuration.setAllowCredentials(true);
                        configuration.addAllowedHeader("*");
                        configuration.setExposedHeaders(List.of("Authorization"));
                        configuration.setMaxAge(3600L);
                        return configuration;
                    }
                })));
		return http.build();
	}
}
