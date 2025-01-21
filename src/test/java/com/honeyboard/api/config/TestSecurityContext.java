package com.honeyboard.api.config;

import org.springframework.stereotype.Component;

@Component
public class TestSecurityContext {
    private static String testToken;
    private static Long testUserId;

    public static String getTestToken() {
        return testToken;
    }

    public static void setTestToken(String token) {
        testToken = token;
    }

    public static Long getTestUserId() {
        return testUserId;
    }

    public static void setTestUserId(Long userId) {
        testUserId = userId;
    }
}
