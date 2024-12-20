package com.honeyboard.api.auth.controller;

import java.sql.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.honeyboard.api.jwt.model.service.JwtService;
import com.honeyboard.api.user.model.User;
import com.honeyboard.api.user.model.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;
    
    @PostMapping("/{domainName}/signup")
    public ResponseEntity<Void> completeOAuth2Signup(
            @PathVariable String domainName, // 구글 네이버 카카오
            @CookieValue("temporary_token") String temporaryToken, // 임시토큰
            @RequestBody User user) { // 클라이언트에서 받은 유저 이름 담아져있음
    	log.debug("AuthController/completeOAuth2Signup");
            
        String email = jwtService.getEmailFromToken(temporaryToken); // OAuth2로 로그인 인증 되어 임시토큰 가지고 컨트롤러로 도착하면 이메일 추출
        
        try {
	        if (userService.existsByEmail(email)) { // 이미 가입되어 있는 이메일이면(애초에 가입된 이메일이면 로그인 로직으로 돌긴 하지만 에러잡기용
	            return ResponseEntity.status(HttpStatus.CONFLICT).build(); // 409 반환
	        }
        } catch(Exception e) {
        	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        
        // 각종 정보 저장
        user.setEmail(email);
        user.setRole("USER");
        user.setLoginType(domainName.toUpperCase());
        
        try {
	        if(userService.saveUser(user)) { // 회원가입
	        	return ResponseEntity.status(HttpStatus.CREATED).build();
	        }
	        
	        return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch(Exception e) {
        	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        
    }
    
    @PostMapping("/signup")
    public ResponseEntity<Void> signup(
    		@RequestBody User user) {
		log.debug("AuthController/signup");
		
		try {
			if(userService.existsByEmail(user.getEmail())) {
				return ResponseEntity.status(HttpStatus.CONFLICT).build();
			}
		} catch(Exception e) {
        	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
		
		user.setLoginType("FORM");
		try {
	        if(userService.saveUser(user)) { // 회원가입
	        	return ResponseEntity.status(HttpStatus.CREATED).build();
	        }
	        
	        return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch(Exception e) {
        	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
