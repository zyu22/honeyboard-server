//package com.honeyboard.api.project.finale.mapper;
//
//import java.util.List;
//
//import org.apache.ibatis.annotations.Param;
//
//public interface FinaleProjectBoardMapper {
//
//	List<FinaleProjectBoard> selectAllFinaleBoards(
//			@Param("finaleId") int finaleId
//	);
//
//	FinaleProjectBoard selectFinaleBoard(int boardId);
//
//	List<FinaleMember> selectAllFinaleMembers(int boardId);
//
//	int insertFinaleBoard(FinaleProjectBoard board);
//
//	int updateFinaleBoard(
//			@Param("boardId") int boardId,
//			@Param("board") FinaleProjectBoard board
//	);
//
//	void updateFinaleBoardSubmitStatus(int boardId);
//
//	int deleteFinaleBoard(int boardId);
//}