package com.honeyboard.api.integration;

import com.honeyboard.api.config.TestConfig;
import com.honeyboard.api.config.TestRedisConfig;
import com.honeyboard.api.config.TestSecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.http.*;
import org.testcontainers.utility.DockerImageName;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import({TestConfig.class, TestSecurityConfig.class, TestRedisConfig.class})
@ActiveProfiles("test")
@Testcontainers
class AlgorithmGuideIntegrationTest {

    @Container
    @ServiceConnection
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withInitScript("sql/server-db-test-init.sql");

    @Container
    @ServiceConnection
    static GenericContainer<?> redis = new GenericContainer<>(DockerImageName.parse("redis:latest"))
            .withExposedPorts(6379);

    @Autowired
    private TestRestTemplate restTemplate;

    HttpHeaders headers = new HttpHeaders();

    @Test
    @DisplayName("기본 테스트: 컨테이너 실행 확인")
    void defaultTest() {
        assertThat(mysql.isRunning())
                .isTrue();
    }
}

