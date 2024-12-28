package com.honeyboard.api.user.controller;

import com.honeyboard.api.user.model.CurrentUser;
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
		try {
			return ResponseEntity.ok(user);
		}catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}

	}

}
