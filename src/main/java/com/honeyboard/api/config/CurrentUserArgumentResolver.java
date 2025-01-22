package com.honeyboard.api.config;

import com.honeyboard.api.jwt.model.service.JwtService;
import com.honeyboard.api.user.model.CurrentUser;
import com.honeyboard.api.util.CookieUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public
class CurrentUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtService jwtService;
    private final CookieUtil cookieUtil;

    @Override
    public
    boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CurrentUser.class);
    }

    @Override
    public
    Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        Cookie cookie = cookieUtil.getCookie(request, "access_token");

        if (cookie != null) {
            String accessToken = cookie.getValue();
            return jwtService.getUserInfoFromToken(accessToken);
        }
        return null;
    }
}
