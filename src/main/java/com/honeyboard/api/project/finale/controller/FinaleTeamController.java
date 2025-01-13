package com.honeyboard.api.project.finale.controller;

import com.honeyboard.api.project.finale.model.request.FinaleProjectTeamUpdate;
import com.honeyboard.api.project.finale.service.FinaleTeamService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/project/finale")
@RequiredArgsConstructor
@Slf4j
public class FinaleTeamController {

	private final FinaleTeamService finaleTeamService;

    // 피날레 프로젝트 팀 수정
    @PutMapping("/{finaleProjectId}/team/{finaleTeamId}")
    public ResponseEntity<Void> updateFinaleProjectTeam(
            @PathVariable int finaleProjectId,
            @PathVariable int finaleTeamId,
            @RequestBody FinaleProjectTeamUpdate request) {
        log.info("피날레 프로젝트 팀 수정 요청 - finaleProjectId: {}, finaleTeamId: {}, request: {}",
                finaleProjectId, finaleTeamId, request);
        finaleTeamService.updateFinaleProjectTeam(finaleProjectId, finaleTeamId, request);
        log.info("피날레 프로젝트 팀 수정 성공");
        return ResponseEntity.ok().build();
    }

}
