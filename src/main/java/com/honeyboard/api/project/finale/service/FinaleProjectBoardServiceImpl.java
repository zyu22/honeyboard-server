package com.honeyboard.api.project.finale.service;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.honeyboard.api.project.finale.mapper.FinaleProjectBoardMapper;
import com.honeyboard.api.project.finale.model.FinaleProjectBoard;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Slf4j
public class FinaleProjectBoardServiceImpl implements FinaleProjectBoardService {

	private final FinaleProjectBoardMapper finaleProjectBoardMapper;

	@Override
	public List<FinaleProjectBoard> getAllFinaleBoards(int finaleId) {
		if (finaleId <= 0) {
			throw new IllegalArgumentException("유효하지 않은 기수 ID입니다.");
		}
		return finaleProjectBoardMapper.selectAllFinaleBoards(finaleId);
	}

	@Override
	public FinaleProjectBoard getFinaleBoard(int boardId) {
		if (boardId <= 0) {
			throw new IllegalArgumentException("유효하지 않은 게시글 ID입니다.");
		}
		return finaleProjectBoardMapper.selectFinaleBoard(boardId);
	}


	@Override
	public boolean addFinaleBoard(FinaleProjectBoard board) {
		validateBoard(board);
		try {
			return finaleProjectBoardMapper.insertFinaleBoard(board) > 0;
		} catch (Exception e) {
			log.error("최종 프로젝트 게시글 등록 실패: {}", e.getMessage());
			throw new RuntimeException("게시글 등록에 실패했습니다.", e);
		}
	}

	@Override
	public boolean updateFinaleBoard(int boardId, FinaleProjectBoard board) {
		if (boardId <= 0) {
			throw new IllegalArgumentException("유효하지 않은 게시글 ID입니다.");
		}
		validateBoard(board);
		try {
			return finaleProjectBoardMapper.updateFinaleBoard(boardId, board) > 0;
		} catch (Exception e) {
			log.error("최종 프로젝트 게시글 수정 실패 - ID: {}, 오류: {}", boardId, e.getMessage());
			throw new RuntimeException("게시글 수정에 실패했습니다.", e);
		}
	}

	@Override
	public void changeFinaleBoardCompletion(int boardId) {
		if (boardId <= 0) {
			throw new IllegalArgumentException("유효하지 않은 게시글 ID입니다.");
		}
		try {
			finaleProjectBoardMapper.updateFinaleBoardSubmitStatus(boardId);
		} catch (Exception e) {
			log.error("최종 프로젝트 게시글 완료 상태 변경 실패 - ID: {}, 오류: {}", boardId, e.getMessage());
			throw new RuntimeException("게시글 상태 변경에 실패했습니다.", e);
		}
	}

	@Override
	public boolean softDeleteFinaleBoard(int boardId) {
		if (boardId <= 0) {
			throw new IllegalArgumentException("유효하지 않은 게시글 ID입니다.");
		}
		try {
			return finaleProjectBoardMapper.deleteFinaleBoard(boardId) > 0;
		} catch (Exception e) {
			log.error("최종 프로젝트 게시글 삭제 실패 - ID: {}, 오류: {}", boardId, e.getMessage());
			throw new RuntimeException("게시글 삭제에 실패했습니다.", e);
		}
	}

	private void validateBoard(FinaleProjectBoard board) {
		if (board == null) {
			throw new IllegalArgumentException("게시글 정보가 없습니다.");
		}
		if (board.getTitle() == null || board.getTitle().trim().isEmpty()) {
			throw new IllegalArgumentException("제목을 입력해주세요.");
		}
		if (board.getContent() == null || board.getContent().trim().isEmpty()) {
			throw new IllegalArgumentException("내용을 입력해주세요.");
		}
		if (board.getAuthorName() == null || board.getAuthorName().trim().isEmpty()) {
			throw new IllegalArgumentException("작성자 정보가 없습니다.");
		}
	}

}
