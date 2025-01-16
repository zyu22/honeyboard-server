package com.honeyboard.api.project.finale.controller;

import com.honeyboard.api.common.model.CreateResponse;
import com.honeyboard.api.project.finale.model.request.FinaleProjectBoardRequest;
import com.honeyboard.api.project.finale.model.response.FinaleProjectBoardDetail;
import com.honeyboard.api.user.model.CurrentUser;
import com.honeyboard.api.user.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.honeyboard.api.project.finale.service.FinaleProjectBoardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/project/finale")
@RequiredArgsConstructor
@Slf4j
public class FinaleProjectBoardController {

	private final FinaleProjectBoardService finaleProjectBoardService;

    // 피날레 게시글 상세 조회
    @GetMapping("/{finaleProjectId}/board/{boardId}")
    public ResponseEntity<FinaleProjectBoardDetail> getFinaleProjectBoardDetail(
            @PathVariable int finaleProjectId,
            @PathVariable int boardId) {
        log.info("피날레 게시글 상세 조회 요청 - finaleProjectId: {}, boardId: {}", finaleProjectId, boardId);
        FinaleProjectBoardDetail boardDetail = finaleProjectBoardService.getFinaleProjectBoardDetail(finaleProjectId, boardId);
        log.info("피날레 게시글 상세 조회 성공");
        return ResponseEntity.ok(boardDetail);
    }

    // 피날레 게시글 작성
    @PostMapping("/{finaleProjectId}/board")
    public ResponseEntity<?> createFinaleProjectBoard(
            @PathVariable int finaleProjectId,
            @RequestBody FinaleProjectBoardRequest request,
            @CurrentUser User currentUser) {
        log.info("피날레 게시글 작성 요청 - finaleProjectId: {}, userId: {}", finaleProjectId, currentUser.getUserId());
        CreateResponse res = new CreateResponse();
        res.setId(finaleProjectBoardService.createFinaleProjectBoard(finaleProjectId, request, currentUser));
        log.info("피날레 게시글 작성 성공");
        return ResponseEntity.ok().body(res);
    }

    // 피날레 프로젝트 게시글 수정
    @PutMapping("/{finaleProjectId}/board/{finaleProjectBoardId}")
    public ResponseEntity<Void> updateFinaleProjectBoard(
            @PathVariable int finaleProjectId,
            @PathVariable int finaleProjectBoardId,
            @RequestBody FinaleProjectBoardRequest request,
            @CurrentUser User currentUser) {
        log.info("게시글 수정 요청 - finaleProjectId: {}, boardId: {}, userId: {}",
                finaleProjectId, finaleProjectBoardId, currentUser.getUserId());

        finaleProjectBoardService.updateFinaleProjectBoard(finaleProjectId, finaleProjectBoardId, request, currentUser);
        return ResponseEntity.ok().build();
    }

}
