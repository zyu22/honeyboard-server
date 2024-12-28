package com.honeyboard.api.project.finale.controller;

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
import org.springframework.web.bind.annotation.RestController;

import com.honeyboard.api.project.finale.model.FinaleProjectBoard;
import com.honeyboard.api.project.finale.service.FinaleProjectBoardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/project/finale")
@RequiredArgsConstructor
public class FinaleProjectBoardController {

	private final FinaleProjectBoardService finaleProjectBoardService;

	@GetMapping("/{finaleId}/board")
	public ResponseEntity<?> getAllFinaleBoards(@PathVariable int finaleId) {
		try {
			List<FinaleProjectBoard> boards = finaleProjectBoardService.getAllFinaleBoards(finaleId);
			if (boards.size() > 0) {
				return ResponseEntity.ok().body(boards);
			}
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@GetMapping("/{finaleId}/board/{boardId}")
	public ResponseEntity<?> getFinaleBoard(@PathVariable int boardId) {
		try {
			FinaleProjectBoard board = finaleProjectBoardService.getFinaleBoard(boardId);
			if (board != null) {
				return ResponseEntity.ok().body(board);
			}
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PostMapping("/{finaleId}/board")
	public ResponseEntity<?> createFinaleBoard(@RequestBody FinaleProjectBoard board) {
		try {
			if (finaleProjectBoardService.addFinaleBoard(board)) {
				return ResponseEntity.ok().body(board);
			}
			return ResponseEntity.badRequest().body("Create failed");
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PutMapping("/{finaleId}/board/{boardId}")
	public ResponseEntity<?> updateFinaleBoard(@PathVariable int boardId, @RequestBody FinaleProjectBoard board) {
		try {
			if (finaleProjectBoardService.updateFinaleBoard(boardId, board)) {
				board.setBoardId(boardId);
				return ResponseEntity.ok().body(board);
			}
			return ResponseEntity.badRequest().body("Update failed");
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@DeleteMapping("/{finaleId}/board/{boardId}")
	public ResponseEntity<?> deleteFinaleBoard(@PathVariable int boardId) {
		try {
			if (finaleProjectBoardService.softDeleteFinaleBoard(boardId)) {
				return ResponseEntity.ok().body("FinaleBoard deleted successfully");
			}
			return ResponseEntity.status(HttpStatus.CONFLICT).body("The board does not exist");
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

}
