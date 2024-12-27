package com.honeyboard.api.project.finale.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.honeyboard.api.project.finale.mapper.FinaleProjectBoardMapper;
import com.honeyboard.api.project.finale.model.FinaleProjectBoard;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FinaleBoardServiceImpl implements FinaleBoardService {

	private final FinaleProjectBoardMapper finaleProjectBoardMapper;

	@Override
	public List<FinaleProjectBoard> getAllFinaleBoards(int finaleId) {
		return finaleProjectBoardMapper.selectAllFinaleBoards(finaleId);
	}

	@Override
	public FinaleProjectBoard getFinaleBoard(int boardId) {
		return finaleProjectBoardMapper.selectFinaleBoard(boardId);
	}

	@Override
	public boolean addFinaleBoard(FinaleProjectBoard board) {
		return finaleProjectBoardMapper.insertFinaleBoard(board) > 0;
	}

	@Override
	public boolean updateFinaleBoard(int boardId, FinaleProjectBoard board) {
		return finaleProjectBoardMapper.updateFinaleBoard(boardId, board) > 0;
	}

	@Override
	public void changeFinaleBoardCompletion(int boardId) {
		finaleProjectBoardMapper.updateFinaleBoardSubmitStatus(boardId);
	}

	@Override
	public boolean softDeleteFinaleBoard(int boardId) {
		return finaleProjectBoardMapper.deleteFinaleBoard(boardId) > 0;
	}

}
