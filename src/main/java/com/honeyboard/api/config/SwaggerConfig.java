package com.honeyboard.api.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("HoneyBoard API Documentation")
                .version("v1.0")
                .description("HoneyBoard 프로젝트의 API 명세서입니다.");

        // Cookie 인증 스키마 설정
        SecurityScheme cookieAuth = new SecurityScheme()
                .type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.COOKIE)
                .name("access_token");

        // Security 요구사항 설정
        SecurityRequirement securityRequirement = new SecurityRequirement().addList("cookieAuth");

        return new OpenAPI()
                .info(info)
                .addSecurityItem(securityRequirement)
                .components(new Components().addSecuritySchemes("cookieAuth", cookieAuth));
    }
}
