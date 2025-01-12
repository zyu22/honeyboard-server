package com.honeyboard.api.user.controller;

import com.honeyboard.api.user.model.CurrentUser;
import com.honeyboard.api.user.model.LogInUserResponse;
import com.honeyboard.api.user.model.User;
import com.honeyboard.api.user.model.bookmark.Bookmark;
import com.honeyboard.api.user.model.mypage.MyTrackProject;
import com.honeyboard.api.user.model.mypage.MyFinaleProject;
import com.honeyboard.api.user.model.mypage.MyAlgorithmSolution;

import com.honeyboard.api.user.service.BookmarkService2;
import com.honeyboard.api.user.service.MyPageService;
import org.springframework.http.HttpStatus;
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
	private final BookmarkService2 bookmarkService;
	private final MyPageService myPageService;


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

	// 북마크 목록 조회
	@GetMapping(("/{userId}/bookmark"))
	public ResponseEntity<?> getBookmarks(
			@PathVariable int userId,
			@RequestParam(name = "contentType") String content_type) {

		log.info("[GET] /api/v1/user/{}/bookmark?contentType={} 요청 수신", userId, content_type);

		List<?> bookmarks = bookmarkService.getAllBookmarks(userId, content_type);

		if (bookmarks.isEmpty()) {
			log.info("북마크가 없습니다. userId={}, contentType={}", userId, content_type);
			return ResponseEntity.noContent().build();
		}

		log.info("북마크 조회 성공 - userId={}, contentType={}, count={}", userId, content_type, bookmarks.size());
		return ResponseEntity.ok(bookmarks);
	}

	//북마크 추가
	@PostMapping("/{userId}/bookmark")
	public ResponseEntity<Void> addBookmark(
			@PathVariable int userId,
			@RequestBody Bookmark bookmark
	) {
		log.info("북마크 추가 요청 - 사용자: {}", userId);
		bookmarkService.addBookmark(userId, bookmark);
		log.info("북마크 추가 완료 - 사용자: {}", userId);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}


	// 북마크 삭제
	@DeleteMapping("/{userId}/bookmark/{contentType}/{contentId}")
	public ResponseEntity<Void> deleteBookmark(
			@PathVariable int userId,
			@PathVariable String contentType,
			@PathVariable int contentId
	) {
		log.info("북마크 요청 시작 - 유저 ID: {}, contentType: {}, contentId: {}", userId, contentType, contentId);

		bookmarkService.deleteBookmark(userId, contentType ,contentId);

		return ResponseEntity.ok().build();
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
