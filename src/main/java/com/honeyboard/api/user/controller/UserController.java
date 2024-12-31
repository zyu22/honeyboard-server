package com.honeyboard.api.user.controller;

import com.honeyboard.api.user.model.CurrentUser;
import com.honeyboard.api.user.model.LogInUserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.honeyboard.api.jwt.model.service.JwtService;
import com.honeyboard.api.user.model.User;
import com.honeyboard.api.user.model.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

	private final JwtService jwtService;
	private final UserService userService;

	@PostMapping("/reset-password")
	public ResponseEntity<Void> updatePassword(
			@CookieValue(name = "temporary_token", required = true) String temporaryToken,
			@RequestBody User requestUser) {
		log.debug("비밀번호 재설정 요청");

		requestUser.setUserId(jwtService.getUserIdFromToken(temporaryToken));
		userService.updatePassword(requestUser);

		return ResponseEntity.ok().build();
	}

	@GetMapping("/info")
	public ResponseEntity<LogInUserResponse> getUserInfo(@CurrentUser User user) {
		log.debug("사용자 정보 조회 요청 - ID: {}", user.getUserId());

		User userInfo = userService.getUser(user.getUserId());
		if (userInfo == null) {
			return ResponseEntity.noContent().build();
		}

		LogInUserResponse response = new LogInUserResponse(
				userInfo.getUserId(),
				userInfo.getName(),
				userInfo.getGenerationId(),
				userInfo.getGenerationName(),
				userInfo.getRole()
		);

		return ResponseEntity.ok(response);
	}

}
