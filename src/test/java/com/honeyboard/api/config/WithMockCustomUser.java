package com.honeyboard.api.config;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithMockCustomUser {
    int id() default 1;
    String email() default "test@test.com";
    String name() default "Test User";
    String role() default "USER";
    int generationId() default 1;
}