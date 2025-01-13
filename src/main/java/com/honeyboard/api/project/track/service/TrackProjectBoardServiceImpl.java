package com.honeyboard.api.project.track.service;

import com.honeyboard.api.common.model.CreateResponse;
import com.honeyboard.api.project.track.mapper.TrackProjectBoardMapper;
import com.honeyboard.api.project.track.model.request.TrackProjectBoardRequest;
import com.honeyboard.api.project.track.model.response.TrackProjectBoardDetail;
import com.honeyboard.api.project.track.model.response.TrackProjectBoardList;
import com.honeyboard.api.project.track.model.response.TrackProjectDetail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.C;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrackProjectBoardServiceImpl implements TrackProjectBoardService{

    private final TrackProjectBoardMapper trackProjectBoardMapper;


    @Override
    public TrackProjectBoardDetail getBoard(int boardId) {
        if (boardId <= 0) {
            throw new IllegalArgumentException("유효하지 않은 게시글 ID입니다.");
        }

        log.debug("게시글 상세 조회 시작 - 게시글ID: {}", boardId);
        return trackProjectBoardMapper.selectTrackProjectBoard(boardId);
    }

    // 관통 게시글 추가
    @Override
    public CreateResponse addBoard(int trackProjectId, int trackTeamId, TrackProjectBoardRequest board) {
        if (trackProjectId <= 0) {
            throw new IllegalArgumentException("유효하지 않은 트랙 프로젝트 ID입니다.");
        }
        validateBoard(board);

        log.info("게시글 생성 시작 - 제목: {}", board.getTitle());
        int boardId = trackProjectBoardMapper.insertTrackProjectBoard(trackProjectId, trackTeamId, board);

        if (boardId == 0) {
            throw new RuntimeException("게시글 생성에 실패했습니다.");
        }

        log.info("게시글 생성 완료 - 게시글 번호: {}", boardId);

        return new CreateResponse(boardId);
    }

    // 관통 게시글 수정
    @Override
    public void updateBoard(int trackProjectId, int trackTeamId, int boardId, TrackProjectBoardRequest board) {
        if (boardId <= 0) {
            throw new IllegalArgumentException("유효하지 않은 게시글 ID입니다.");
        }
        validateBoard(board);


        log.info("게시글 수정 시작 - ID: {}", boardId);
        int result = trackProjectBoardMapper.updateTrackProjectBoard(trackProjectId, trackTeamId, boardId, board);

        if (result != 1) {
            throw new RuntimeException("게시글 수정에 실패했습니다.");
        }

        log.info("게시글 수정 완료 - ID: {}", boardId);
    }

    // 관통 게시글 삭제
    @Override
    public void softDeleteBoard(int boardId) {
        if (boardId <= 0) {
            throw new IllegalArgumentException("유효하지 않은 게시글 ID입니다.");
        }

        log.info("게시글 삭제 시작 - ID: {}", boardId);
        int result = trackProjectBoardMapper.deleteTrackProjectBoard(boardId);

        if (result != 1) {
            throw new RuntimeException("게시글 삭제에 실패했습니다.");
        }

        log.info("게시글 삭제 완료 - ID: {}", boardId);
    }

    private void validateBoard(TrackProjectBoardRequest board) {
        if (board == null) {
            throw new IllegalArgumentException("게시글 정보가 없습니다.");
        }
        if (board.getTitle() == null || board.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("제목을 입력해주세요.");
        }
        if (board.getContent() == null || board.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("내용을 입력해주세요.");
        }
    }
}
