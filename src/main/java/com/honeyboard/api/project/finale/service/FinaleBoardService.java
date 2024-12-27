package com.honeyboard.api.project.finale.service;

import java.util.List;

import com.honeyboard.api.project.finale.model.FinaleProjectBoard;

public interface FinaleBoardService {

	List<FinaleProjectBoard> getAllFinaleBoards(int finaleId);

	FinaleProjectBoard getFinaleBoard(int boardId);

	boolean addFinaleBoard(FinaleProjectBoard board);

	boolean updateFinaleBoard(int boardId, FinaleProjectBoard board);

	void changeFinaleBoardCompletion(int boardId);

	boolean softDeleteFinaleBoard(int boardId);

}
