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
    public ResponseEntity<?> getAllTrackBoard(@PathVariable int trackId){
        try{
            List<TrackProjectBoard> boards = ts.getAllBoard(trackId);
            if(boards.size() > 0) {
                return ResponseEntity.status(HttpStatus.OK).body(boards);
            }
            return ResponseEntity.noContent().build();
        } catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("{trackId}/board/{boardId}")
    public ResponseEntity<?> getTrackBoard(@PathVariable int trackId, @PathVariable int boardId) {
        try {
            TrackProjectBoard board = ts.getBoard(boardId);
            if (board != null) {
                return ResponseEntity.ok().body(board);
            }
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{trackId}/board")
    public ResponseEntity<?> createTrackProjectBoard(
            @PathVariable int trackId, @RequestBody TrackProjectBoard board){
        try{
            TrackProjectBoard createdBoard = ts.addBoard(trackId,board);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdBoard);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{trackId}/board/{boardId}")
    public ResponseEntity<?> updateTrackProjectBoard(@PathVariable int trackId, @PathVariable int boardId, @RequestBody TrackProjectBoard board){
        try{
            TrackProjectBoard updatedBoard = ts.updateBoard(boardId,board);
            return ResponseEntity.ok().body(updatedBoard);
        } catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{trackId}/board/{boardId}")
    public ResponseEntity<?> deleteTrackProjectBoard(@PathVariable int trackId, @PathVariable int boardId){
        try{
            if(ts.softDeleteBoard(boardId)){
                return ResponseEntity.ok().body("게시글 삭제 성공");
            }
            return ResponseEntity.status(HttpStatus.CONFLICT).body("존재하지 않는 게시글입니다");
        } catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
