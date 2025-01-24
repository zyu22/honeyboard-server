package com.honeyboard.api.project.finale.mapper;

import java.util.List;

import com.honeyboard.api.project.finale.model.FinaleProjectBoard;
import com.honeyboard.api.project.finale.model.request.FinaleProjectBoardRequest;
import com.honeyboard.api.project.finale.model.response.FinaleProjectBoardDetail;
import com.honeyboard.api.project.finale.model.response.FinaleProjectBoardList;
import org.apache.ibatis.annotations.Param;

public interface FinaleProjectBoardMapper {

    // 프로젝트 상세 내 게시글 조회
    List<FinaleProjectBoardList> selectFinaleProjectDetailBoards(int finaleProjectId);

    FinaleProjectBoardDetail selectFinaleProjectBoardDetail(
            @Param("finaleProjectId") int finaleProjectId,
            @Param("boardId") int boardId
    );

    boolean checkFinaleProjectBoard(@Param("finaleProjectId") int finaleProjectId,
                                    @Param("boardId") int boardId);

    int insertFinaleProjectBoard(@Param("finaleProjectId") int finaleProjectId,
                                 @Param("board") FinaleProjectBoard board);

    int updateFinaleProjectBoard(@Param("finaleProjectId") int finaleProjectId,
                                 @Param("finaleProjectBoardId") int finaleProjectBoardId,
                                 @Param("request") FinaleProjectBoardRequest request);

    int selectLastInsertedBoardId();

    int deleteFinaleProjectBoard(@Param("finaleProjectId") int finaleProjectId,
                                 @Param("finaleProjectBoardId") int finaleProjectBoardId);
}