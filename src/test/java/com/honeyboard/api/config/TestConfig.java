package com.honeyboard.api.config;

import com.honeyboard.api.user.model.CurrentUser;
import com.honeyboard.api.user.model.User;  // User 클래스 import 추가
import org.springframework.boot.test.context.TestConfiguration;  // TestConfiguration import 수정
import org.springframework.context.annotation.Bean;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@TestConfiguration
public class TestConfig {
    @Bean
    public HandlerMethodArgumentResolver customUserArgumentResolver() {
        return new HandlerMethodArgumentResolver() {
            @Override
            public boolean supportsParameter(MethodParameter parameter) {
                return parameter.hasParameterAnnotation(CurrentUser.class);
            }

            @Override
            public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                          NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
                User mockUser = new User();
                mockUser.setUserId(8);
                return mockUser;
            }
        };
    }
}