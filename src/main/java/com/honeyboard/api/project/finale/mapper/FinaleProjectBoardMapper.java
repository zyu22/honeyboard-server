package com.honeyboard.api.project.finale.mapper;

import java.util.List;

import com.honeyboard.api.project.finale.model.FinaleMember;
import com.honeyboard.api.project.finale.model.FinaleProjectBoard;

public interface FinaleProjectBoardMapper {

	List<FinaleProjectBoard> selectAllFinalBoards(int finaleId);

	FinaleProjectBoard selectFinalBoard(int boardId);

	List<FinaleMember> selectAllFinaleMembers(int boardId);

	int insertFinalBoard(FinaleProjectBoard board);

	int updateFinalBoard(int boardId, FinaleProjectBoard board);

	void updateFinalBoardSubmitStatus(int boardId);

	int deleteFinalBoard(int boardId);

}
