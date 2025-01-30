package com.honeyboard.api.project.finale.controller;

import com.honeyboard.api.project.finale.model.request.FinaleProjectTeamUpdate;
import com.honeyboard.api.project.finale.service.FinaleTeamService;
import com.honeyboard.api.project.model.ProjectUserInfo;
import com.honeyboard.api.project.model.TeamRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@RestController
@RequestMapping("/api/v1/project/finale")
@RequiredArgsConstructor
@Slf4j
public class FinaleTeamController {

	private final FinaleTeamService finaleTeamService;

    // 피날레 프로젝트 팀 수정
    @PutMapping("/team/{finaleTeamId}")
    public ResponseEntity<Void> updateFinaleProjectTeam(
            @RequestBody TeamRequest request) {
        log.info("피날레 프로젝트 팀 수정 요청 - request: {}", request.toString());
        finaleTeamService.updateFinaleProjectTeam(request);
        log.info("피날레 프로젝트 팀 수정 성공");
        return ResponseEntity.ok().build();
    }

    // 피날레 가능인원 조회
    @GetMapping("/available-members")
    public ResponseEntity<?> getAvailableMembers() {
        log.info("피날레 프로젝트 팀 생성 가능 인원 조회 요청");
        List<ProjectUserInfo> availableUsers = finaleTeamService.getFinaleTeamUsers();
        log.info("피날레 프로젝트 팀 생성 가능 인원 조회 성공");
        return ResponseEntity.ok(availableUsers);
    }
}
