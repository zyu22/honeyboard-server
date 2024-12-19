package com.honeyboard.api.user.model.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.honeyboard.api.user.model.CustomUserDetails;
import com.honeyboard.api.user.model.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

	private final UserService userService;
	
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    	OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService(); // 기본 OAuth2 사용자 서비스 생성
        OAuth2User oAuth2User = delegate.loadUser(userRequest); // OAuth2 제공자로부터 사용자 정보 로드

        String registrationId = userRequest.getClientRegistration().getRegistrationId(); // OAuth2 제공자 ID 추출 (google, kakao, naver 등)
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName(); // OAuth2 제공자가 사용하는 고유 식별자 필드명 추출
        
        Map<String, Object> attributes = oAuth2User.getAttributes(); // OAuth2 제공자로부터 받은 사용자 속성 정보
        String email = (String) attributes.get("email"); // 이메일 정보 추출
        
        User existingUser = userService.getUserByEmail(email); // 기존 사용자 조회
        
        if (existingUser == null) { // 신규 사용자인 경우
            Map<String, Object> customAttributes = new HashMap<>(attributes);
            customAttributes.put("isNewUser", true); // 신규 사용자 표시
            customAttributes.put("loginType", registrationId.toUpperCase()); // 로그인 제공자 정보
            
            User tempUser = User.builder() // 임시 사용자 객체 생성
                    .email(email)
                    .role("USER")
                    .build();
                    
                return new CustomUserDetails(tempUser, customAttributes); // 사용자 상세 정보 반환
            }
            
            return new CustomUserDetails(existingUser, attributes); // 기존 사용자인 경우 해당 사용자 정보로 CustomUserDetails 생성하여 반환
    }

}
