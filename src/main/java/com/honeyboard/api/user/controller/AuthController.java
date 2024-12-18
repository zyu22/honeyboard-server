package com.honeyboard.api.user.controller;

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

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;
    
    @PostMapping("/{domainName}/signup")
    public ResponseEntity<Void> completeOAuth2Signup(
            @PathVariable String domainName,
            @CookieValue("temporary_token") String temporaryToken,
            @RequestBody User user) {
            
        String email = jwtService.getEmailFromToken(temporaryToken);
        
        if (userService.checkEmail(email) > 0) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        
        user.setEmail(email);
        user.setRole("USER");
        user.setLoginType(domainName.toUpperCase());
        user.setSsafy(true);
        user.setCreatedAt(new Date(System.currentTimeMillis()));
        
        userService.addUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
