package com.honeyboard.api.project.finale.mapper;

import java.util.List;

import com.honeyboard.api.project.finale.model.FinaleMember;
import com.honeyboard.api.project.finale.model.FinaleProjectBoard;

public interface FinaleProjectBoardMapper {

	List<FinaleProjectBoard> selectAllFinaleBoards(int finaleId);

	FinaleProjectBoard selectFinaleBoard(int boardId);

	List<FinaleMember> selectAllFinaleMembers(int boardId);

	int insertFinaleBoard(FinaleProjectBoard board);

	int updateFinaleBoard(int boardId, FinaleProjectBoard board);

	void updateFinaleBoardSubmitStatus(int boardId);

	int deleteFinaleBoard(int boardId);

}
