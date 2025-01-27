package com.honeyboard.api.util;

import com.honeyboard.api.config.TestConfig;
import com.honeyboard.api.config.TestRedisConfig;
import com.honeyboard.api.config.TestSecurityConfig;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.lang.annotation.*;

@Target(ElementType.TYPE)  // 클래스 레벨에 적용
@Retention(RetentionPolicy.RUNTIME)  // 런타임까지 유지
@Inherited  // 상속 가능하도록 설정
@Documented  // JavaDoc에 포함

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Testcontainers
@AutoConfigureWebTestClient
@Sql(scripts = "classpath:sql/test-clean.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:sql/test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Import({TestConfig.class,
        TestSecurityConfig.class,
        TestRedisConfig.class,
})
public @interface IntegrationTest {
}
