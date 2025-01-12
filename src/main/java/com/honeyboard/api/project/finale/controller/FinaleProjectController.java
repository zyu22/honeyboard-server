package com.honeyboard.api.project.finale.controller;

import com.honeyboard.api.project.finale.model.request.FinaleProjectBoardRequest;
import com.honeyboard.api.project.finale.model.request.FinaleProjectCreate;
import com.honeyboard.api.project.finale.model.request.FinaleProjectTeamUpdate;
import com.honeyboard.api.project.finale.model.request.FinaleProjectUpdate;
import com.honeyboard.api.project.finale.model.response.FinaleProjectBoardDetail;
import com.honeyboard.api.project.finale.model.response.FinaleProjectDetail;
import com.honeyboard.api.project.finale.model.response.FinaleProjectResponse;
import com.honeyboard.api.project.finale.service.FinaleProjectService;
import com.honeyboard.api.user.model.CurrentUser;
import com.honeyboard.api.user.model.User;
import com.honeyboard.api.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/project/finale")
@RequiredArgsConstructor
@Slf4j
public class FinaleProjectController {

    private final
    FinaleProjectService finaleProjectService;
    private final
    UserService userService;

    // 피날레 프로젝트 전체 조회
    @GetMapping
    public
    ResponseEntity<?> getFinaleProjectResponse(@RequestParam(defaultValue = "0") int generationId) {
        log.info("피날레 프로젝트 전체 조회 시작 generationId: {}", generationId);
        if(generationId == 0) generationId = userService.getActiveGenerationId();
        FinaleProjectResponse res = finaleProjectService.getFinaleResponse(generationId);
        if(res == null) {
            log.info("프로젝트가 존재하지 않습니다.");
            return ResponseEntity.noContent().build();
        }

        log.info("피날레 프로젝트 전체 조회 성공");
        return ResponseEntity.ok().body(res);
    }

    // 피날레 프로젝트 상세 조회
    @GetMapping("/api/v1/project/finale/{finaleProjectId}")
    public ResponseEntity<FinaleProjectDetail> getFinaleProjectDetail(@PathVariable int finaleProjectId) {
        log.info("피날레 프로젝트 상세 조회 요청 - finaleProjectId: {}", finaleProjectId);
        FinaleProjectDetail projectDetail = finaleProjectService.getFinaleProjectDetail(finaleProjectId);
        log.info("피날레 프로젝트 상세 조회 성공");
        return ResponseEntity.ok(projectDetail);
    }

    // 피날레 게시글 상세 조회
    @GetMapping("/api/v1/project/finale/{finaleProjectId}/board/{boardId}")
    public ResponseEntity<FinaleProjectBoardDetail> getFinaleProjectBoardDetail(
            @PathVariable int finaleProjectId,
            @PathVariable int boardId) {
        log.info("피날레 게시글 상세 조회 요청 - finaleProjectId: {}, boardId: {}", finaleProjectId, boardId);
        FinaleProjectBoardDetail boardDetail = finaleProjectService.getFinaleProjectBoardDetail(finaleProjectId, boardId);
        log.info("피날레 게시글 상세 조회 성공");
        return ResponseEntity.ok(boardDetail);
    }

    // 피날레 팀 + 프로젝트 생성
    @PostMapping
    public ResponseEntity<?> createFinaleProject(@RequestBody FinaleProjectCreate req) {
        log.info("피날레 팀 + 프로젝트 생성 시작");
        finaleProjectService.createFinaleProject(req);
        log.info("피날레 팀 + 프로젝트 생성 성공");
        return ResponseEntity.ok().build();
    }

    // 피날레 게시글 작성
    @PostMapping("/api/v1/project/finale/{finaleProjectId}/board")
    public ResponseEntity<Void> createFinaleProjectBoard(
            @PathVariable int finaleProjectId,
            @RequestBody FinaleProjectBoardRequest request,
            @CurrentUser User currentUser) {
        log.info("피날레 게시글 작성 요청 - finaleProjectId: {}, userId: {}", finaleProjectId, currentUser.getUserId());
        finaleProjectService.createFinaleProjectBoard(finaleProjectId, request, currentUser);
        log.info("피날레 게시글 작성 성공");
        return ResponseEntity.ok().build();
    }

    // 피날레 프로젝트 수정
    @PutMapping("/{finaleProjectId}")
    public ResponseEntity<Void> updateFinaleProject(
            @PathVariable int finaleProjectId,
            @RequestBody FinaleProjectUpdate request) {
        log.info("피날레 프로젝트 수정 요청 - finaleProjectId: {}, request: {}", finaleProjectId, request);
        finaleProjectService.updateFinaleProject(finaleProjectId, request);
        log.info("피날레 프로젝트 수정 성공");
        return ResponseEntity.ok().build();
    }

    // 피날레 프로젝트 팀 수정
    @PutMapping("/api/v1/project/finale/{finaleProjectId}/team/{finaleTeamId}")
    public ResponseEntity<Void> updateFinaleProjectTeam(
            @PathVariable int finaleProjectId,
            @PathVariable int finaleTeamId,
            @RequestBody FinaleProjectTeamUpdate request) {
        log.info("피날레 프로젝트 팀 수정 요청 - finaleProjectId: {}, finaleTeamId: {}, request: {}",
                finaleProjectId, finaleTeamId, request);
        finaleProjectService.updateFinaleProjectTeam(finaleProjectId, finaleTeamId, request);
        log.info("피날레 프로젝트 팀 수정 성공");
        return ResponseEntity.ok().build();
    }

    // 피날레 프로젝트 삭제
    @DeleteMapping("/api/v1/project/finale/team/{finaleProjectId}")
    public ResponseEntity<Void> deleteFinaleProject(@PathVariable int finaleProjectId) {
        log.info("피날레 프로젝트 삭제 요청 - finaleProjectId: {}", finaleProjectId);
        finaleProjectService.deleteFinaleProject(finaleProjectId);
        log.info("피날레 프로젝트 삭제 성공");
        return ResponseEntity.ok().build();
    }

}
