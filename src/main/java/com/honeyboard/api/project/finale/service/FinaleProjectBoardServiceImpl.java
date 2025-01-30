package com.honeyboard.api.project.finale.service;

import com.honeyboard.api.exception.BusinessException;
import com.honeyboard.api.exception.ErrorCode;
import com.honeyboard.api.project.finale.mapper.FinaleProjectBoardMapper;
import com.honeyboard.api.project.finale.mapper.FinaleProjectMapper;
import com.honeyboard.api.project.finale.mapper.FinaleTeamMapper;
import com.honeyboard.api.project.finale.model.FinaleProjectBoard;
import com.honeyboard.api.project.finale.model.request.FinaleProjectBoardRequest;
import com.honeyboard.api.project.finale.model.response.FinaleProjectBoardDetail;
import com.honeyboard.api.user.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FinaleProjectBoardServiceImpl implements FinaleProjectBoardService {

    private final FinaleProjectBoardMapper finaleProjectBoardMapper;
    private final FinaleTeamMapper finaleTeamMapper;
    private final FinaleProjectMapper finaleProjectMapper;

    @Override
    @Transactional(readOnly = true)
    public
    FinaleProjectBoardDetail getFinaleProjectBoardDetail(int finaleProjectId, int boardId) {
        log.info("게시글 상세 조회 시작 - finaleProjectId: {}, boardId: {}", finaleProjectId, boardId);
        validateBoardDetail(finaleProjectId, boardId);
        FinaleProjectBoardDetail boardDetail = finaleProjectBoardMapper.selectFinaleProjectBoardDetail(finaleProjectId, boardId);
        log.info("게시글 상세 조회 완료 - finaleProjectId: {}, boardId: {}", finaleProjectId, boardId);
        return boardDetail;
    }

    @Override
    @Transactional
    public int createFinaleProjectBoard(int finaleProjectId, FinaleProjectBoardRequest request, User currentUser) {
        log.info("게시글 작성 시작 - finaleProjectId: {}, userId: {}", finaleProjectId, currentUser.getUserId());

        Integer finaleTeamId = finaleTeamMapper.selectFinaleTeamId(finaleProjectId);
        if (finaleTeamId == null) {
            throw new IllegalArgumentException("해당 프로젝트의 팀 정보를 찾을 수 없습니다.");
        }

        boolean isTeamMember = finaleTeamMapper.checkTeamMember(finaleTeamId, currentUser.getUserId());
        if (!isTeamMember) {
            throw new IllegalArgumentException("해당 팀의 멤버만 게시글을 작성할 수 있습니다.");
        }

        validateBoardCreate(finaleProjectId, request, currentUser);

        FinaleProjectBoard board = new FinaleProjectBoard();
        board.setInfos(finaleTeamId, currentUser.getUserId(), request);
        int result = finaleProjectBoardMapper.insertFinaleProjectBoard(finaleProjectId, board);

        if (result <= 0) {
            throw new IllegalArgumentException("요청 정보가 잘못되었습니다.");
        }

        log.info("게시글 작성 완료 - finaleProjectId: {}", finaleProjectId);
        return finaleProjectBoardMapper.selectLastInsertedBoardId();
    }

    @Override
    @Transactional
    public boolean updateFinaleProjectBoard(int finaleProjectId, int finaleProjectBoardId, FinaleProjectBoardRequest request, User currentUser) {
        log.info("게시글 수정 시작 - finaleProjectId: {}, boardId: {}, userId: {}",
                finaleProjectId, finaleProjectBoardId, currentUser.getUserId());

        if(currentUser.getRole().equals("USER")) validateBoardUpdate(finaleProjectId, finaleProjectBoardId, request, currentUser);

        int result = finaleProjectBoardMapper.updateFinaleProjectBoard(finaleProjectId, finaleProjectBoardId, request);

        if (result <= 0) {
            throw new BusinessException(ErrorCode.PROJECT_TEAM_UPDATE_FAILED, "요청 정보가 잘못되었습니다.");
        }

        log.info("게시글 수정 완료 - finaleProjectId: {}, boardId: {}", finaleProjectId, finaleProjectBoardId);
        return true;
    }

    @Override
    @Transactional
    public boolean deleteFinaleProjectBoard(int finaleProjectId, int finaleProjectBoardId, User currentUser) {
        log.info("게시글 삭제 시작 - finaleProjectId: {}, boardId: {}", finaleProjectId, finaleProjectBoardId);

        if(currentUser.getRole().equals("USER")) {
            Integer finaleTeamId = finaleTeamMapper.selectFinaleTeamId(finaleProjectId);
            if (finaleTeamId == null) {
                throw new BusinessException(ErrorCode.INVALID_TEAM_ID, "해당 프로젝트의 팀 정보를 찾을 수 없습니다.");
            }

            boolean isTeamMember = finaleTeamMapper.checkTeamMember(finaleTeamId, currentUser.getUserId()) || currentUser.getRole().equals("ADMIN");
            if (!isTeamMember) {
                throw new BusinessException(ErrorCode.PROJECT_DELETE_FAILED, "해당 팀의 멤버만 게시글을 삭제할 수 있습니다.");
            }
        }

        int result = finaleProjectBoardMapper.deleteFinaleProjectBoard(finaleProjectId, finaleProjectBoardId);

        if(result <= 0) {
            throw new BusinessException(ErrorCode.PROJECT_DELETE_FAILED, "요청 정보가 잘못되었습니다.");
        }

        log.info("게시글 삭제 완료 - finaleProjectId: {}, boardId: {}", finaleProjectId, finaleProjectBoardId);
        return true;
    }

    private void validateBoardDetail(int finaleProjectId, int boardId) {
        if (!finaleProjectMapper.checkFinaleProject(finaleProjectId)) {
            throw new BusinessException(ErrorCode.PROJECT_NOT_FOUND,
                    String.format("프로젝트를 찾을 수 없습니다. (프로젝트 ID: %d)", finaleProjectId));
        }

        if (!finaleProjectBoardMapper.checkFinaleProjectBoard(finaleProjectId, boardId)) {
            throw new BusinessException(ErrorCode.BOARD_NOT_FOUND,
                    String.format("게시글을 찾을 수 없습니다. (프로젝트 ID: %d, 게시글 ID: %d)", finaleProjectId, boardId));
        }
    }

    private void validateBoardCreate(int finaleProjectId, FinaleProjectBoardRequest request, User currentUser) {
        if (!finaleProjectMapper.checkFinaleProject(finaleProjectId)) {
            throw new BusinessException(ErrorCode.PROJECT_NOT_FOUND,
                    String.format("프로젝트를 찾을 수 없습니다. (프로젝트 ID: %d)", finaleProjectId));
        }

        int teamId = finaleTeamMapper.selectFinaleTeamId(finaleProjectId);

        if (!finaleTeamMapper.checkTeamMember(teamId, currentUser.getUserId())) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED_TEAM_MEMBER,
                    "해당 팀의 멤버만 게시글을 작성할 수 있습니다.");
        }

        if (request.getTitle() == null || request.getTitle().trim().isEmpty()) {
            throw new BusinessException(ErrorCode.BOARD_TITLE_REQUIRED);
        }

        if (request.getContent() == null || request.getContent().trim().isEmpty()) {
            throw new BusinessException(ErrorCode.BOARD_CONTENT_REQUIRED);
        }
    }

    private void validateBoardUpdate(int finaleProjectId, int finaleProjectBoardId, FinaleProjectBoardRequest request, User currentUser) {
        if (!finaleProjectMapper.checkFinaleProject(finaleProjectId)) {
            throw new BusinessException(ErrorCode.PROJECT_NOT_FOUND);
        }

        if (!(finaleTeamMapper.checkBoardTeamMember(finaleProjectId, finaleProjectBoardId, currentUser.getUserId()) || currentUser.getRole().equals("ADMIN"))) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED_BOARD_UPDATE);
        }

        if (request.getTitle() == null || request.getTitle().trim().isEmpty()) {
            throw new BusinessException(ErrorCode.BOARD_TITLE_REQUIRED);
        }

        if (request.getContent() == null || request.getContent().trim().isEmpty()) {
            throw new BusinessException(ErrorCode.BOARD_CONTENT_REQUIRED);
        }
    }

}
