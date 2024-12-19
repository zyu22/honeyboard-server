package com.honeyboard.api.user.model.util;

import java.util.Map;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Component;

import com.honeyboard.api.user.model.oauth2.GoogleOAuth2UserInfo;
import com.honeyboard.api.user.model.oauth2.KakaoOAuth2UserInfo;
import com.honeyboard.api.user.model.oauth2.NaverOAuth2UserInfo;
import com.honeyboard.api.user.model.oauth2.OAuth2UserInfo;

@Component
public class OAuth2UserInfoFactory {
    public static OAuth2UserInfo getOAuth2UserInfo(String domainName, Map<String, Object> attributes) {
        return switch (domainName.toLowerCase()) { // OAuth2 제공자에 따라 진행
            case "google" -> new GoogleOAuth2UserInfo(attributes);
            case "naver" -> new NaverOAuth2UserInfo(attributes);
            case "kakao" -> new KakaoOAuth2UserInfo(attributes);
            default -> throw new AuthenticationServiceException(
                    "Unsupported login type: " + domainName);
        };
    }
}