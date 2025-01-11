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

import com.honeyboard.api.project.finale.service.FinaleProjectBoardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/project/finale")
@RequiredArgsConstructor
public class FinaleProjectBoardController {

	private final FinaleProjectBoardService finaleProjectBoardService;

	@GetMapping("/{finaleId}/board")
	public ResponseEntity<List<FinaleProjectBoard>> getAllFinaleBoards(@PathVariable int finaleId) {
		List<FinaleProjectBoard> boards = finaleProjectBoardService.getAllFinaleBoards(finaleId);
		return boards.isEmpty() ? ResponseEntity.noContent().build()
				: ResponseEntity.ok(boards);
	}

	@GetMapping("/{finaleId}/board/{boardId}")
	public ResponseEntity<FinaleProjectBoard> getFinaleBoard(@PathVariable int boardId) {
		FinaleProjectBoard board = finaleProjectBoardService.getFinaleBoard(boardId);
		return board == null ? ResponseEntity.noContent().build()
				: ResponseEntity.ok(board);
	}

	@PostMapping("/{finaleId}/board")
	public ResponseEntity<FinaleProjectBoard> createFinaleBoard(@RequestBody FinaleProjectBoard board) {
		finaleProjectBoardService.addFinaleBoard(board);
		return ResponseEntity.status(HttpStatus.CREATED).body(board);
	}

	@PutMapping("/{finaleId}/board/{boardId}")
	public ResponseEntity<FinaleProjectBoard> updateFinaleBoard(
			@PathVariable int boardId,
			@RequestBody FinaleProjectBoard board) {
		finaleProjectBoardService.updateFinaleBoard(boardId, board);
		board.setBoardId(boardId);
		return ResponseEntity.ok(board);
	}

	@DeleteMapping("/{finaleId}/board/{boardId}")
	public ResponseEntity<Void> deleteFinaleBoard(@PathVariable int boardId) {
		finaleProjectBoardService.softDeleteFinaleBoard(boardId);
		return ResponseEntity.ok().build();
	}

}
