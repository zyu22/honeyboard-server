package com.honeyboard.api.project.track.controller;

import com.honeyboard.api.common.model.CreateResponse;
import com.honeyboard.api.project.track.model.request.TrackProjectBoardRequest;
import com.honeyboard.api.project.track.model.response.TrackProjectBoardDetail;
import com.honeyboard.api.project.track.model.response.TrackProjectBoardList;
import com.honeyboard.api.project.track.service.TrackProjectBoardService;
import com.honeyboard.api.user.model.CurrentUser;
import com.honeyboard.api.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/project/track")
public class TrackProjectBoardController {
    private final TrackProjectBoardService trackProjectBoardService;

    // 관통 게시글 조회
    @GetMapping("{trackProjectId}/team/{trackTeamId}/board/{boardId}")
    public ResponseEntity<TrackProjectBoardDetail> getTrackBoard(
            @PathVariable int trackProjectId,
            @PathVariable int trackTeamId,
            @PathVariable int boardId) {
        TrackProjectBoardDetail board = trackProjectBoardService.getBoard(boardId);
        return board == null ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(board);
    }

    // 관통 게시글 작성
    @PostMapping("/{trackProjectId}/team/{trackTeamId}/board")
    public ResponseEntity<?> createTrackProjectBoard(
            @PathVariable int trackProjectId,
            @PathVariable int trackTeamId,
            @RequestBody TrackProjectBoardRequest board,
            @CurrentUser User user) {
        // 사용자 ID가 null인 경우 처리
        if (user == null || user.getUserId() == 0) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        CreateResponse createResponse = trackProjectBoardService.addBoard(trackProjectId, trackTeamId, user.getUserId(), board);
        return ResponseEntity.status(HttpStatus.CREATED).body(createResponse);
    }

    // 관통 게시글 수정
    @PutMapping("/{trackProjectId}/team/{trackTeamId}/board/{boardId}")
    public ResponseEntity<?> updateTrackProjectBoard(
            @PathVariable int trackProjectId,
            @PathVariable int trackTeamId,
            @PathVariable int boardId,
            @RequestBody TrackProjectBoardRequest board) {
        trackProjectBoardService.updateBoard(trackProjectId, trackTeamId, boardId, board);
        return ResponseEntity.ok().build();
    }

    // 관통 게시글 삭제
    @DeleteMapping("/{trackProjectId}/track/{trackTeamId}/board/{boardId}")
    public ResponseEntity<Void> deleteTrackProjectBoard(
            @PathVariable int trackProjectId,
            @PathVariable int trackTeamId,
            @PathVariable int boardId) {
        trackProjectBoardService.softDeleteBoard(trackProjectId, trackTeamId, boardId);
        return ResponseEntity.ok().build();
    }

}
