package com.honeyboard.api.project.finale.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

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
import com.honeyboard.api.project.finale.service.FinaleTeamService;
import com.honeyboard.api.user.model.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/project/finale")
@RequiredArgsConstructor
@Slf4j
public class FinaleTeamController {

	private final FinaleTeamService finaleTeamService;

	@GetMapping("/{projectId}/status")
	public ResponseEntity<?> getAllSubmittedStatus(@PathVariable int projectId,
			@RequestParam(required = false) String date) {
		String targetDate = (date != null && !date.isEmpty()) ? date
				: LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		try {
			List<FinaleTeam> submittedStatus = finaleTeamService.findStatusByDate(targetDate);
			if (submittedStatus.size() > 0) {
				return ResponseEntity.ok().body(submittedStatus);
			}
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@GetMapping("/team/remaining")
	public ResponseEntity<?> getRemainedUsers(@RequestParam(required = false) int generationId) {
		try {
			List<User> remainedUsers = finaleTeamService.getRemainedUsers(generationId);
			if (remainedUsers.size() > 0) {
				return ResponseEntity.ok().body(remainedUsers);
			}
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PostMapping("/team")
	public ResponseEntity<?> createFinaleProject(@RequestBody FinaleProject finaleProject) {
		try {
			if (finaleTeamService.saveFinaleProject(finaleProject)) {
				return ResponseEntity.ok().body("FinaleProject created successfully");
			}
			return ResponseEntity.badRequest().body("The data is inadequate");
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PutMapping("/team/update")
	public ResponseEntity<?> updateFinaleProject(@RequestBody FinaleProject finaleProject) {
		try {
			if (finaleTeamService.updateFinaleProject(finaleProject)) {
				return ResponseEntity.ok().body("FinaleProject updated successfully");
			}
			return ResponseEntity.badRequest().body("Update failed");
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@DeleteMapping("/team/{teamId}")
	public ResponseEntity<?> removeFinaleProject(@PathVariable int teamId) {
		try {
			if (finaleTeamService.removeFinaleProject(teamId)) {
				return ResponseEntity.ok().body("FinaleProject removed successfully");
			}
			return ResponseEntity.status(HttpStatus.CONFLICT).body("The Project does not exist");
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

}
