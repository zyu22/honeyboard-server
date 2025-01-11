//package com.honeyboard.api.project.finale.controller;
//
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import java.util.List;
//
//import com.honeyboard.api.project.finale.service.FinaleTeamService;
//import com.honeyboard.api.user.service.UserService;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.honeyboard.api.user.model.UserName;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//
//@RestController
//@RequestMapping("/api/v1/project/finale/team")
//@RequiredArgsConstructor
//@Slf4j
//public class FinaleTeamController {
//
//	private final FinaleTeamService finaleTeamService;
//	private final UserService userService;
//
//	@GetMapping("/{projectId}/status")
//	public ResponseEntity<List<FinaleTeam>> getAllSubmittedStatus(
//			@PathVariable int projectId,
//			@RequestParam(required = false) String date) {
//
//		LocalDate targetDate = date != null && !date.isEmpty()
//				? LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyyMMdd"))
//				: LocalDate.now();
//
//		List<FinaleTeam> submittedStatus = finaleTeamService.findStatusByDate(targetDate);
//		return submittedStatus.isEmpty() ? ResponseEntity.noContent().build()
//				: ResponseEntity.ok(submittedStatus);
//	}
//
//	@GetMapping("/remaining")
//	public ResponseEntity<List<UserName>> getRemainedUsers(
//			@RequestParam(required = false) Integer generationId) {
//
//		if (generationId == null) {
//			generationId = userService.getActiveGenerationId();
//		}
//
//		List<UserName> remainedUsers = finaleTeamService.getRemainedUsers(generationId);
//		return remainedUsers.isEmpty() ? ResponseEntity.noContent().build()
//				: ResponseEntity.ok(remainedUsers);
//	}
//
//	@PostMapping
//	public ResponseEntity<FinaleTeam> createTeam(@RequestBody FinaleTeamRequest request) {
//		finaleTeamService.createTeam(request);
//		return ResponseEntity.status(HttpStatus.CREATED).build();
//	}
//
//	@PutMapping("/update/{id}")
//	public ResponseEntity<FinaleTeam> updateTeam(
//			@PathVariable int id,
//			@RequestBody FinaleTeamRequest request) {
//			request.setTeamId(id);
//			finaleTeamService.updateTeam(request);
//			return ResponseEntity.ok().build();
//	}
//
//}
