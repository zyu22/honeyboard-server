package com.honeyboard.api.user.model.oauth2;

import java.util.Map;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class NaverOAuth2UserInfo implements OAuth2UserInfo {
	
	private final Map<String, Object> attributes;

	public NaverOAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = (Map<String, Object>) attributes.get("response");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }

    @Override
    public String getLoginType() {
        return "NAVER";
    }

}
