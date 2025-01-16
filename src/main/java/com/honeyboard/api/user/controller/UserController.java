package com.honeyboard.api.user.controller;

import com.honeyboard.api.bookmark.service.BookmarkService;
import com.honeyboard.api.user.model.CurrentUser;
import com.honeyboard.api.user.model.LogInUserResponse;
import com.honeyboard.api.user.model.User;
import com.honeyboard.api.user.model.mypage.MyTrackProject;
import com.honeyboard.api.user.model.mypage.MyFinaleProject;
import com.honeyboard.api.user.model.mypage.MyAlgorithmSolution;

import com.honeyboard.api.user.service.MyPageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.honeyboard.api.jwt.model.service.JwtService;
import com.honeyboard.api.user.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

	private final JwtService jwtService;
	private final UserService userService;
	private final BookmarkService bookmarkService;
	private final MyPageService myPageService;


	@PostMapping("/reset-password")
	public ResponseEntity<Void> updatePassword(
			@CookieValue(name = "temporary_token", required = true) String temporaryToken,
			@RequestBody User requestUser) {
		log.debug("비밀번호 재설정 요청");

		requestUser.setUserId(
				userService.getUserByEmail(
						jwtService.extractUserEmail(temporaryToken))
						.getUserId());
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



	@GetMapping("/{userId}/trackproject")
	public ResponseEntity<List<MyTrackProject>> getMyTrackProjects(@PathVariable int userId) {
		log.info("[GET] /api/v1/user/{}/trackproject 요청 수신", userId);

		List<MyTrackProject> projects = myPageService.getAllMyTrackProjects(userId);
		if (projects.isEmpty()) {
			log.info("나의 트랙 프로젝트가 없습니다. userId={}", userId);
			return ResponseEntity.noContent().build();
		}

		log.info("나의 트랙 프로젝트 조회 성공 - userId={}, count={}", userId, projects.size());
		return ResponseEntity.ok(projects);
	}

	@GetMapping("/{userId}/finaleproject")
	public ResponseEntity<List<MyFinaleProject>> getMyFinaleProjects(@PathVariable int userId) {
		log.info("[GET] /api/v1/user/{}/finaleproject 요청 수신", userId);

		List<MyFinaleProject> finaleProjects = myPageService.getAllMyFinaleProjects(userId);
		if (finaleProjects.isEmpty()) {
			log.info("나의 파이널 프로젝트가 없습니다. userId={}", userId);
			return ResponseEntity.noContent().build();
		}

		log.info("나의 파이널 프로젝트 조회 성공 - userId={}, count={}", userId, finaleProjects.size());
		return ResponseEntity.ok(finaleProjects);
	}

	@GetMapping("/{userId}/algorithm")
	public ResponseEntity<List<MyAlgorithmSolution>> getMyAlgorithms(@PathVariable int userId) {
		log.info("[GET] /api/v1/user/{}/algorithm 요청 수신", userId);

		List<MyAlgorithmSolution> solutions = myPageService.getAllMyAlgorithms(userId);
		if (solutions.isEmpty()) {
			log.info("나의 알고리즘 풀이가 없습니다. userId={}", userId);
			return ResponseEntity.noContent().build();
		}

		log.info("나의 알고리즘 조회 성공 - userId={}, count={}", userId, solutions.size());
		return ResponseEntity.ok(solutions);
	}


}
