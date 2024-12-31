package com.honeyboard.api.user.service;

import com.honeyboard.api.user.model.CustomUserDetails;
import com.honeyboard.api.user.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("사용자 인증 시도: {}", email);

        User user = userService.getUserByEmail(email);
        if (user == null) {
            String errorMessage = String.format("이메일 '%s'에 해당하는 사용자를 찾을 수 없습니다.", email);
            log.error(errorMessage);
            throw new UsernameNotFoundException(errorMessage);
        }

        log.info("사용자 '{}' 인증 성공", email);
        return new CustomUserDetails(user);
    }
}