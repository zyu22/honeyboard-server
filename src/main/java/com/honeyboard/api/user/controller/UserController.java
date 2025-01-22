package com.honeyboard.api.user.controller;

import com.honeyboard.api.user.model.CurrentUser;
import com.honeyboard.api.user.model.LogInUserResponse;
import com.honeyboard.api.user.model.User;
import com.honeyboard.api.user.model.mypage.MyAlgorithmSolutionList;
import com.honeyboard.api.user.model.mypage.MyFinaleProjectList;

import com.honeyboard.api.user.model.mypage.MyTrackProjectList;
import com.honeyboard.api.user.service.MyPageService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
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
	private final MyPageService myPageService;
	private final
	PasswordEncoder passwordEncoder;

	@PostMapping("/reset-password")
	public ResponseEntity<Void> updatePassword(
			@CookieValue(name = "temporary_token", required = true) String temporaryToken,
			@RequestBody User requestUser) {
		log.debug("비밀번호 재설정 요청");

		requestUser.setUserId(
				userService.getUserByEmail(
						jwtService.extractUserEmail(temporaryToken))
						.getUserId());
		requestUser.setPassword(passwordEncoder.encode(requestUser.getPassword()));
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



	@GetMapping("/trackProject")
	public ResponseEntity<List<MyTrackProjectList>> getMyTrackProjects(@CurrentUser User user) {
		log.info("내 관통 프로젝트 조회 요청 - 유저 ID : {}", user.getUserId());

		List<MyTrackProjectList> trackProjects = myPageService.getAllMyTrackProjects(user.getUserId());
		if (trackProjects.isEmpty() || trackProjects.size() == 0) {
			log.info("내 관통 프로젝트 조회 완료, 데이터 없음 - 유저 ID: {}", user.getUserId());
			return ResponseEntity.noContent().build();
		}
		log.info("내 관통 프로젝트 조회 완료 - 유저 ID: {}, 개수: {}", user.getUserId(), trackProjects.size());
		return ResponseEntity.ok(trackProjects);
	}

	@GetMapping("/finaleproject")
	public ResponseEntity<List<MyFinaleProjectList>> getMyFinaleProjects(@CurrentUser User user) {
		log.info("내 파이널 프로젝트 조회 요청 - 유저 ID: {}", user.getUserId());

		List<MyFinaleProjectList> finaleProjects = myPageService.getAllMyFinaleProjects(user.getUserId());
		if (finaleProjects.isEmpty() || finaleProjects.size() == 0) {
			log.info("내 파이널 프로젝트 조회 완료, 데이터없음 - 유저 ID: {}", user.getUserId());
			return ResponseEntity.noContent().build();
		}
		log.info("내 파이널 프로젝트 조회 완료 - 유저 ID: {}, 개수: {}", user.getUserId(), finaleProjects.size());
		return ResponseEntity.ok(finaleProjects);
	}

	@GetMapping("/algorithmSolution")
	public ResponseEntity<List<MyAlgorithmSolutionList>> getMyAlgorithms(@CurrentUser User user) {
		log.info("나의 알고리즘 조회 요청 - 유저 ID: {}", user.getUserId());

		List<MyAlgorithmSolutionList> solutions = myPageService.getAllMyAlgorithms(user.getUserId());
		if (solutions.isEmpty() || solutions.size() == 0) {
			log.info("나의 알고리즘 조회 완료, 데이터 없음 - 유저 ID: {}", user.getUserId());
			return ResponseEntity.noContent().build();
		}

		log.info("나의 알고리즘 조회 완료 - 유저 ID: {}, 개수: {}", user.getUserId(), solutions.size());
		return ResponseEntity.ok(solutions);
	}


}
