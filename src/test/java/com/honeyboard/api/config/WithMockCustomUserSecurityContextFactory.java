package com.honeyboard.api.config;

import com.honeyboard.api.user.model.CustomUserDetails;
import com.honeyboard.api.user.model.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Collections;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {
    
    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        // Create User object with annotation values
        User user = new User();
        user.setUserId(annotation.id());
        user.setEmail(annotation.email());
        user.setName(annotation.name());
        user.setRole(annotation.role());
        user.setGenerationId(annotation.generationId());

        // Create CustomUserDetails with the User object
        CustomUserDetails principal = new CustomUserDetails(user, Collections.emptyMap());

        // Create Authentication token
        Authentication auth = new UsernamePasswordAuthenticationToken(
            principal,
            "password", // credentials (not needed for testing)
            principal.getAuthorities()
        );

        // Set Authentication in SecurityContext
        context.setAuthentication(auth);

        return context;
    }
}