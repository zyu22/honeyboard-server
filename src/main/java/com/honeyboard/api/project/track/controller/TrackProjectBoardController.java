package com.honeyboard.api.project.track.controller;

import com.honeyboard.api.project.track.model.TrackProjectBoard;
import com.honeyboard.api.project.track.service.TrackProjectBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/project/track")
public class TrackProjectBoardController {
    private final TrackProjectBoardService ts;

    @GetMapping("/{trackId}/board")
    public ResponseEntity<List<TrackProjectBoard>> getAllTrackBoard(@PathVariable int trackId) {
        List<TrackProjectBoard> boards = ts.getAllBoard(trackId);
        return boards.isEmpty() ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(boards);
    }

    @GetMapping("{trackId}/board/{boardId}")
    public ResponseEntity<TrackProjectBoard> getTrackBoard(
            @PathVariable int trackId,
            @PathVariable int boardId) {
        TrackProjectBoard board = ts.getBoard(boardId);
        return board == null ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(board);
    }

    @PostMapping("/{trackId}/board")
    public ResponseEntity<TrackProjectBoard> createTrackProjectBoard(
            @PathVariable int trackId,
            @RequestBody TrackProjectBoard board) {
        TrackProjectBoard createdBoard = ts.addBoard(trackId, board);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBoard);
    }

    @PutMapping("/{trackId}/board/{boardId}")
    public ResponseEntity<TrackProjectBoard> updateTrackProjectBoard(
            @PathVariable int trackId,
            @PathVariable int boardId,
            @RequestBody TrackProjectBoard board) {
        TrackProjectBoard updatedBoard = ts.updateBoard(boardId, board);
        return ResponseEntity.ok(updatedBoard);
    }

    @DeleteMapping("/{trackId}/board/{boardId}")
    public ResponseEntity<Void> deleteTrackProjectBoard(
            @PathVariable int trackId,
            @PathVariable int boardId) {
        ts.softDeleteBoard(boardId);
        return ResponseEntity.ok().build();
    }

}
