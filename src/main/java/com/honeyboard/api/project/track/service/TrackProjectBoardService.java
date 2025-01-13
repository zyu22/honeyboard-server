package com.honeyboard.api.project.track.service;

import com.honeyboard.api.common.model.CreateResponse;
import com.honeyboard.api.project.track.model.request.TrackProjectBoardRequest;
import com.honeyboard.api.project.track.model.response.TrackProjectBoardDetail;
import com.honeyboard.api.project.track.model.response.TrackProjectBoardList;
import com.honeyboard.api.project.track.model.response.TrackProjectDetail;

import java.util.List;

public interface TrackProjectBoardService {
    // 관통 게시글 상세조회
    TrackProjectBoardDetail getBoard(int boardId);

    // 관통 게시글 작성
    CreateResponse addBoard(int trackProjectId, int trackTeamId, TrackProjectBoardRequest board);

    // 관통 게시글 수정
    void updateBoard(int trackProjectId, int trackTeamId, int boardId, TrackProjectBoardRequest board);

    // 관통 게시글 삭제
    void softDeleteBoard(int boardId);
}
