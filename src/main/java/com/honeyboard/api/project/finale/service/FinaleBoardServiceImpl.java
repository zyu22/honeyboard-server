package com.honeyboard.api.project.finale.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.honeyboard.api.project.finale.mapper.FinaleProjectBoardMapper;
import com.honeyboard.api.project.finale.model.FinaleProjectBoard;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FinaleBoardServiceImpl implements FinaleBoardService {

	private final FinaleProjectBoardMapper finalBoardMapper;

	@Override
	public List<FinaleProjectBoard> getAllFinaleBoards(int finaleId) {
		return finalBoardMapper.selectAllFinaleBoards(finaleId);
	}

	@Override
	public FinaleProjectBoard getFinaleBoard(int boardId) {
		return finalBoardMapper.selectFinaleBoard(boardId);
	}

	@Override
	public boolean addFinaleBoard(FinaleProjectBoard board) {
		return finalBoardMapper.insertFinaleBoard(board) > 0;
	}

	@Override
	public boolean updateFinaleBoard(int boardId, FinaleProjectBoard board) {
		return finalBoardMapper.updateFinaleBoard(boardId, board) > 0;
	}

	@Override
	public void changeFinaleBoardCompletion(int boardId) {
		finalBoardMapper.updateFinaleBoardSubmitStatus(boardId);
	}

	@Override
	public boolean softDeleteFinaleBoard(int boardId) {
		return finalBoardMapper.deleteFinaleBoard(boardId) > 0;
	}

}
