package com.honeyboard.api.user.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.honeyboard.api.user.model.CustomUserDetails;
import com.honeyboard.api.user.model.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
	
	private final UserService userService;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userService.getUserByEmail(email); // email(유저의 아이디)로 정보 가져오기
		if (user == null) { // 없는 사용자면
            throw new UsernameNotFoundException("User not found");
        }
        return new CustomUserDetails(user);
	}

}
