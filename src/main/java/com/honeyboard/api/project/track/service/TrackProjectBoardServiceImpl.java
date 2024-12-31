package com.honeyboard.api.project.track.service;

import com.honeyboard.api.project.track.mapper.TrackProjectBoardMapper;
import com.honeyboard.api.project.track.model.TrackProject;
import com.honeyboard.api.project.track.model.TrackProjectBoard;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrackProjectBoardServiceImpl implements TrackProjectBoardService{

    private final TrackProjectBoardMapper trackProjectBoardMapper;

    @Override
    public List<TrackProjectBoard> getAllBoard(int trackProjectId) {
        if (trackProjectId <= 0) {
            throw new IllegalArgumentException("유효하지 않은 트랙 프로젝트 ID입니다.");
        }

        log.debug("게시글 전체 조회 시작 - 트랙id: {}", trackProjectId);
        return trackProjectBoardMapper.selectAllTrackProjectsBoards(trackProjectId);
    }

    @Override
    public TrackProjectBoard getBoard(int boardId) {
        if (boardId <= 0) {
            throw new IllegalArgumentException("유효하지 않은 게시글 ID입니다.");
        }

        log.debug("게시글 상세 조회 시작 - 게시글ID: {}", boardId);
        return trackProjectBoardMapper.selectTrackProjectBoard(boardId);
    }

    @Override
    public TrackProjectBoard addBoard(int trackProjectId, TrackProjectBoard board) {
        if (trackProjectId <= 0) {
            throw new IllegalArgumentException("유효하지 않은 트랙 프로젝트 ID입니다.");
        }
        validateBoard(board);

        try {
            log.debug("게시글 생성 시작 - 제목: {}", board.getTitle());
            int result = trackProjectBoardMapper.insertTrackProjectBoard(trackProjectId, board);

            if (result != 1) {
                throw new RuntimeException("게시글 생성에 실패했습니다.");
            }

            log.info("게시글 생성 완료 - 제목: {}", board.getTitle());
            return board;
        } catch (Exception e) {
            log.error("게시글 생성 실패 - 제목: {}, 오류: {}", board.getTitle(), e.getMessage());
            throw new RuntimeException("게시글 생성 중 오류가 발생했습니다.", e);
        }
    }

    @Override
    public TrackProjectBoard updateBoard(int boardId, TrackProjectBoard board) {
        if (boardId <= 0) {
            throw new IllegalArgumentException("유효하지 않은 게시글 ID입니다.");
        }
        validateBoard(board);

        try {
            log.debug("게시글 수정 시작 - ID: {}", boardId);
            int result = trackProjectBoardMapper.updateTrackProjectBoard(boardId, board);

            if (result != 1) {
                throw new RuntimeException("게시글 수정에 실패했습니다.");
            }

            log.info("게시글 수정 완료 - ID: {}", boardId);
            return board;
        } catch (Exception e) {
            log.error("게시글 수정 실패 - ID: {}, 오류: {}", boardId, e.getMessage());
            throw new RuntimeException("게시글 수정 중 오류가 발생했습니다.", e);
        }
    }

    @Override
    public boolean softDeleteBoard(int boardId) {
        if (boardId <= 0) {
            throw new IllegalArgumentException("유효하지 않은 게시글 ID입니다.");
        }

        try {
            log.debug("게시글 삭제 시작 - ID: {}", boardId);
            int result = trackProjectBoardMapper.deleteTrackProjectBoard(boardId);

            if (result != 1) {
                throw new RuntimeException("게시글 삭제에 실패했습니다.");
            }

            log.info("게시글 삭제 완료 - ID: {}", boardId);
            return true;
        } catch (Exception e) {
            log.error("게시글 삭제 실패 - ID: {}, 오류: {}", boardId, e.getMessage());
            throw new RuntimeException("게시글 삭제 중 오류가 발생했습니다.", e);
        }
    }

    private void validateBoard(TrackProjectBoard board) {
        if (board == null) {
            throw new IllegalArgumentException("게시글 정보가 없습니다.");
        }
        if (board.getTitle() == null || board.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("제목을 입력해주세요.");
        }
        if (board.getContent() == null || board.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("내용을 입력해주세요.");
        }
        if (board.getTeamMembers() == null || board.getTeamMembers().trim().isEmpty()) {
            throw new IllegalArgumentException("팀원 정보를 입력해주세요.");
        }
    }

}
