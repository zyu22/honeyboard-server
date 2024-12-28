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
        log.info("게시글 전체 조회 시작 - 트랙id: {}", trackProjectId);

        List<TrackProjectBoard> boards = trackProjectBoardMapper.selectAllTrackProjectsBoards(trackProjectId);

        if (boards.isEmpty()) {
            log.warn("해당 트랙에 게시글이 존재하지 않음 - 트랙id: {}", trackProjectId);
        } else {
            log.info("게시글 조회 완료 - 트랙id: {}, 게시글 수: {}", trackProjectId, boards.size());
        }

        return boards;
    }

    @Override
    public TrackProjectBoard getBoard(int boardId) {
        log.info("게시글 상세 조회 시작 - 게시글ID : {}", boardId);

        TrackProjectBoard board = trackProjectBoardMapper.selectTrackProjectBoard(boardId);

        if (board == null) {
            log.warn("게시글 없음 - 게시글ID : {}", boardId);
        } else {
            log.info("게시글 상세 조회 성공 - 게시글ID : {}", boardId);
        }

        return board;
    }

    @Override
    public TrackProjectBoard addBoard(int trackProjectId, TrackProjectBoard board) {
        log.info("게시글 생성 - 제목 : {}", board.getTitle());
        int result = trackProjectBoardMapper.insertTrackProjectBoard(trackProjectId, board);
        if(result != 1) {
            log.error("게시글 생성 실패 - 제목: {}", board.getTitle());
            throw new RuntimeException("게시글 생성 실패");
        }
        log.info("게시글 생성 완료: {}", board.getTitle());
        return board;
    }

    @Override
    public TrackProjectBoard updateBoard(int boardId, TrackProjectBoard board) {
        log.info("게시글 수정 시작 - ID: {}", boardId);
        int result = trackProjectBoardMapper.updateTrackProjectBoard(boardId, board);
        if(result != 1) {
            log.error("게시글 수정 실패 - ID: {}", boardId);
        }
        log.info("게시글 수정 완료: {}", boardId);
        return board;
    }

    @Override
    public boolean softDeleteBoard(int boardId) {
        int result = trackProjectBoardMapper.deleteTrackProjectBoard(boardId);
        if(result != 0){
            log.error("게시글 삭제 실패 - ID: {}", boardId);
        }
        log.info("게시글 삭제 완료: {}", boardId);
        return result==1;
    }
}
