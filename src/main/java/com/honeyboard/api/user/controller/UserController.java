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
		log.debug("UserController/updatePassword");
		try {
			requestUser.setUserId(jwtService.getUserIdFromToken(temporaryToken));
			if (userService.updatePassword(requestUser)) {
				return ResponseEntity.ok().build();
			}
			return ResponseEntity.badRequest().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@GetMapping("/info")
	public ResponseEntity<?> getUserInfo(
			@CurrentUser User user) {
		log.debug("유저컨트롤러");
		try {
			User temp = userService.getUser(user.getUserId());
			log.debug("유저 정보 가져옴");
			LogInUserResponse loginUser = new LogInUserResponse(
					temp.getUserId(),
					temp.getName(),
					temp.getGenerationId(),
					temp.getGenerationName(),
					temp.getRole());
			return ResponseEntity.ok(loginUser);
		}catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}

	}

}
