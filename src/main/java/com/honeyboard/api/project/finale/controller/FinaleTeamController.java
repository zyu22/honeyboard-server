package com.honeyboard.api.project.finale.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.honeyboard.api.project.finale.model.FinaleProjectResponse;
import com.honeyboard.api.project.finale.service.FinaleTeamService;
import com.honeyboard.api.user.mapper.UserMapper;
import com.honeyboard.api.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.honeyboard.api.project.finale.model.FinaleProject;
import com.honeyboard.api.project.finale.model.FinaleTeam;
import com.honeyboard.api.project.finale.service.FinaleProjectService;
import com.honeyboard.api.user.model.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/project/finale")
@RequiredArgsConstructor
@Slf4j
public class FinaleTeamController {

	private final FinaleProjectService finaleProjectService;
	private final FinaleTeamService finaleTeamService;
	private final UserMapper userMapper;
	private final UserService userService;

	@GetMapping("/{projectId}/status")
	public ResponseEntity<List<FinaleTeam>> getAllSubmittedStatus(
			@PathVariable int projectId,
			@RequestParam(required = false) String date) {

		LocalDate targetDate = date != null && !date.isEmpty()
				? LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyyMMdd"))
				: LocalDate.now();

		List<FinaleTeam> submittedStatus = finaleTeamService.findStatusByDate(targetDate);
		return submittedStatus.isEmpty() ? ResponseEntity.noContent().build()
				: ResponseEntity.ok(submittedStatus);
	}

	@GetMapping("/team/remaining")
	public ResponseEntity<List<User>> getRemainedUsers(
			@RequestParam(required = false) Integer generationId) {

		if (generationId == null) {
			generationId = userService.getActiveGenerationId();
		}

		List<User> remainedUsers = finaleTeamService.getRemainedUsers(generationId);
		return remainedUsers.isEmpty() ? ResponseEntity.noContent().build()
				: ResponseEntity.ok(remainedUsers);
	}

	@GetMapping()
	public ResponseEntity<FinaleProjectResponse> getAllFinaleProjects(
			@RequestParam(required = false) Integer generationId) {

		if (generationId == null) {
			generationId = userService.getActiveGenerationId();
		}

		List<FinaleProject> projects = finaleProjectService.getAllFinaleProject(generationId);
		if (projects.isEmpty()) {
			return ResponseEntity.noContent().build();
		}

		List<User> users = userService.getAllUsersWithTeamInfo(generationId);
		List<FinaleTeam> submits = finaleTeamService.findStatusByDate(LocalDate.now());

		FinaleProjectResponse response = new FinaleProjectResponse(projects, users, submits);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/team")
	public ResponseEntity<FinaleProject> createFinaleProject(
			@RequestBody FinaleProject finaleProject) {

		finaleProjectService.saveFinaleProject(finaleProject);
		return ResponseEntity.status(HttpStatus.CREATED).body(finaleProject);
	}

	@PutMapping("/team/update")
	public ResponseEntity<FinaleProject> updateFinaleProject(
			@RequestBody FinaleProject finaleProject) {

		finaleProjectService.updateFinaleProject(finaleProject);
		return ResponseEntity.ok(finaleProject);
	}

	@DeleteMapping("/team/{finaleProjectId}")
	public ResponseEntity<Void> deleteFinaleProject(@PathVariable int finaleProjectId) {
		finaleProjectService.softDeleteFinaleProject(finaleProjectId);
		return ResponseEntity.ok().build();
	}

}
