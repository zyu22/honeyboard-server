package com.honeyboard.api.auth.model.oauth2;

import java.util.Map;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GoogleOAuth2UserInfo implements OAuth2UserInfo {

	private final Map<String, Object> attributes;

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
		return "GOOGLE";
	}

}
