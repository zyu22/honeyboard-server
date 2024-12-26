package com.honeyboard.api.project.finale.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.honeyboard.api.project.finale.mapper.FinaleProjectBoardMapper;
import com.honeyboard.api.project.finale.model.FinaleProjectBoard;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FinaleBoardServiceImpl implements FinaleBoardService {

	private final FinaleProjectBoardMapper finalProjectBoardMapper;

	@Override
	public List<FinaleProjectBoard> getAllFinaleBoards(int finaleId) {
		return finalProjectBoardMapper.selectAllFinaleBoards(finaleId);
	}

	@Override
	public FinaleProjectBoard getFinaleBoard(int boardId) {
		return finalProjectBoardMapper.selectFinaleBoard(boardId);
	}

	@Override
	public boolean addFinaleBoard(FinaleProjectBoard board) {
		return finalProjectBoardMapper.insertFinaleBoard(board) > 0;
	}

	@Override
	public boolean updateFinaleBoard(int boardId, FinaleProjectBoard board) {
		return finalProjectBoardMapper.updateFinaleBoard(boardId, board) > 0;
	}

	@Override
	public void changeFinaleBoardCompletion(int boardId) {
		finalProjectBoardMapper.updateFinaleBoardSubmitStatus(boardId);
	}

	@Override
	public boolean softDeleteFinaleBoard(int boardId) {
		return finalProjectBoardMapper.deleteFinaleBoard(boardId) > 0;
	}

}
