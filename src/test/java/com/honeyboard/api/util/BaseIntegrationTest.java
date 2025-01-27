package com.honeyboard.api.util;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

import com.honeyboard.api.jwt.model.service.JwtService;
import com.honeyboard.api.user.model.User;

@IntegrationTest
public abstract class BaseIntegrationTest {
    @Container
    @ServiceConnection
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withInitScript("sql/test-init.sql");

    @Container
    @ServiceConnection
    static GenericContainer<?> redis = new GenericContainer<>(DockerImageName.parse("redis:latest"))
            .withExposedPorts(6379);

    @Autowired
    protected WebTestClient webTestClient;
    @Autowired
    protected JwtService jwtService;
    @Autowired
    protected JdbcTemplate jdbcTemplate;

    @LocalServerPort
    protected int port;

    protected HttpHeaders headers = new HttpHeaders();
    protected String baseUrl;

    @BeforeEach
    protected void setup() {
        // 기본 헤더만 설정
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 테스트용 사용자 생성
        User user = User.builder()
                .email("test3@test.com")
                .userId(3)
                .role("USER")
                .generationId(2)
                .build();

        // JWT 토큰 생성
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        // WebTestClient 설정
        webTestClient = webTestClient.mutate()
                .defaultHeader(HttpHeaders.COOKIE,
                    String.format("access_token=%s; refresh_token=%s", accessToken, refreshToken))
                .build();

        // SecurityContext에 인증 정보 설정
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user,
                null,
                Collections.singleton(new SimpleGrantedAuthority("ROLE_" + user.getRole())));
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        // baseUrl 설정
        baseUrl = "http://localhost:" + port + "/api/v1";
    }

}
