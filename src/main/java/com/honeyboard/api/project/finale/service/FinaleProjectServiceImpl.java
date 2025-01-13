package com.honeyboard.api.project.finale.service;

import java.util.List;

import com.honeyboard.api.exception.BusinessException;
import com.honeyboard.api.exception.ErrorCode;
import com.honeyboard.api.project.finale.mapper.FinaleProjectMapper;
import com.honeyboard.api.project.finale.model.request.FinaleProjectBoardRequest;
import com.honeyboard.api.project.finale.model.request.FinaleProjectCreate;
import com.honeyboard.api.project.finale.model.request.FinaleProjectTeamUpdate;
import com.honeyboard.api.project.finale.model.request.FinaleProjectUpdate;
import com.honeyboard.api.project.finale.model.response.*;
import com.honeyboard.api.project.model.ProjectUserInfo;
import com.honeyboard.api.user.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FinaleProjectServiceImpl implements FinaleProjectService {

    private final FinaleProjectMapper finaleProjectMapper;

    @Override
    public
    FinaleProjectResponse getFinaleResponse(int generationId) {
        log.info("FinaleResponse 가져오기 시작 generationId: {}", generationId);
        FinaleProjectResponse fpr = new FinaleProjectResponse();
        List<FinaleProjectList> projects = getFinaleProjectList(generationId);
        if(projects == null) {
            log.info("프로젝트가 없습니다.");
            return null;
        }
        log.info("FinaleResponse 가져오기 성공");
        int finaleProjectId = projects.get(0).getId();
        fpr.setProjects(projects);
        fpr.setNoTeamUsers(getNoFinaleTeamUsers(finaleProjectId));
        fpr.setTeams(finaleProjectMapper.selectFinaleTeamList(finaleProjectId));
        return fpr;
    }

    @Override
    @Transactional
    public int createFinaleProject(FinaleProjectCreate request) {
        log.info("프로젝트 생성 시작");

        // 유효성 검사
        validateProjectCreate(request);

        // 프로젝트와 팀 멤버 동시 생성
        int result = finaleProjectMapper.insertFinaleProject(request);

        if (result <= 0) {
            log.error("프로젝트 생성 실패");
            throw new RuntimeException("프로젝트 생성에 실패했습니다.");
        }

        log.info("프로젝트 생성 완료");
        return finaleProjectMapper.selectLastInsertedId();
    }

    @Override
    @Transactional
    public boolean updateFinaleProject(int finaleProjectId, FinaleProjectUpdate request) {
        log.info("프로젝트 수정 시작 - finaleProjectId: {}", finaleProjectId);

        validateUpdateRequest(finaleProjectId, request);

        int result = finaleProjectMapper.updateFinaleProject(finaleProjectId, request);
        if (result <= 0) {
            throw new BusinessException(ErrorCode.PROJECT_UPDATE_FAILED);
        }

        log.info("프로젝트 수정 완료 - finaleProjectId: {}", finaleProjectId);
        return result > 0;
    }

    @Override
    @Transactional
    public boolean deleteFinaleProject(int finaleProjectId) {
        log.info("프로젝트 삭제 시작 - finaleProjectId: {}", finaleProjectId);

        // 프로젝트 존재 여부 확인
        if (!finaleProjectMapper.checkFinaleProject(finaleProjectId)) {
            throw new BusinessException(ErrorCode.PROJECT_NOT_FOUND,
                    String.format("프로젝트 ID %d를 찾을 수 없습니다.", finaleProjectId));
        }

        // 논리적 삭제 수행
        int result = finaleProjectMapper.updateFinaleProjectDeleteStatus(finaleProjectId);

        if (result <= 0) {
            log.error("프로젝트 삭제 실패 - finaleProjectId: {}", finaleProjectId);
            throw new BusinessException(ErrorCode.PROJECT_DELETE_FAILED);
        }

        log.info("프로젝트 삭제 완료 - finaleProjectId: {}", finaleProjectId);
        return result > 0;
    }

    @Override
    @Transactional(readOnly = true)
    public FinaleProjectDetail getFinaleProjectDetail(int finaleProjectId) {
        log.info("프로젝트 상세 조회 시작 - finaleProjectId: {}", finaleProjectId);

        // 프로젝트 존재 여부 확인
        if (!finaleProjectMapper.checkFinaleProject(finaleProjectId)) {
            throw new BusinessException(ErrorCode.PROJECT_NOT_FOUND,
                    String.format("프로젝트 ID %d를 찾을 수 없습니다.", finaleProjectId));
        }

        // 기본 정보 조회
        FinaleProjectDetail projectDetail = finaleProjectMapper.selectFinaleProjectDetail(finaleProjectId);

        // 팀 멤버 정보 조회
        projectDetail.setMembers(finaleProjectMapper.selectFinaleProjectDetailMembers(projectDetail.getFinaleTeamId()));

        // 게시판 목록 조회
        projectDetail.setBoards(finaleProjectMapper.selectFinaleProjectDetailBoards(finaleProjectId));

        log.info("프로젝트 상세 조회 완료 - finaleProjectId: {}", finaleProjectId);
        return projectDetail;
    }

    @Override
    @Transactional
    public boolean updateFinaleProjectTeam(int finaleProjectId, int finaleTeamId, FinaleProjectTeamUpdate request) {
        log.info("팀 프로젝트 수정 시작 - finaleProjectId: {}, finaleTeamId: {}", finaleProjectId, finaleTeamId);

        validateProjectTeamUpdate(finaleProjectId, request);

        int result = finaleProjectMapper.updateFinaleProjectTeam(finaleProjectId, finaleTeamId, request);
        if (result <= 0) {
            throw new BusinessException(ErrorCode.PROJECT_TEAM_UPDATE_FAILED);
        }

        log.info("팀 프로젝트 수정 완료 - finaleProjectId: {}, finaleTeamId: {}", finaleProjectId, finaleTeamId);
        return result > 0;
    }

    @Override
    @Transactional(readOnly = true)
    public FinaleProjectBoardDetail getFinaleProjectBoardDetail(int finaleProjectId, int boardId) {
        log.info("게시글 상세 조회 시작 - finaleProjectId: {}, boardId: {}", finaleProjectId, boardId);

        // 유효성 검사 수행
        validateBoardDetail(finaleProjectId, boardId);

        // 게시글 상세 조회
        FinaleProjectBoardDetail boardDetail = finaleProjectMapper.selectFinaleProjectBoardDetail(finaleProjectId, boardId);

        log.info("게시글 상세 조회 완료 - finaleProjectId: {}, boardId: {}", finaleProjectId, boardId);
        return boardDetail;
    }

    @Override
    @Transactional
    public int createFinaleProjectBoard(int finaleProjectId, FinaleProjectBoardRequest request, User currentUser) {
        log.info("게시글 작성 시작 - finaleProjectId: {}, userId: {}", finaleProjectId, currentUser.getUserId());

        validateBoardCreate(finaleProjectId, request, currentUser);

        int result = finaleProjectMapper.insertFinaleProjectBoard(finaleProjectId, request);

        if (result <= 0) {
            throw new BusinessException(ErrorCode.BOARD_CREATE_FAILED);
        }

        log.info("게시글 작성 완료 - finaleProjectId: {}", finaleProjectId);
        return finaleProjectMapper.selectLastInsertedBoardId();
    }

    @Override
    @Transactional
    public boolean updateFinaleProjectBoard(int finaleProjectId, int finaleProjectBoardId,
                                         FinaleProjectBoardRequest request, User currentUser) {
        log.info("게시글 수정 시작 - finaleProjectId: {}, boardId: {}, userId: {}",
                finaleProjectId, finaleProjectBoardId, currentUser.getUserId());

        validateBoardUpdate(finaleProjectId, finaleProjectBoardId, request, currentUser);

        int result = finaleProjectMapper.updateFinaleProjectBoard(finaleProjectId, finaleProjectBoardId, request);

        if (result <= 0) {
            throw new BusinessException(ErrorCode.BOARD_UPDATE_FAILED);
        }

        log.info("게시글 수정 완료 - finaleProjectId: {}, boardId: {}", finaleProjectId, finaleProjectBoardId);
        return result > 0;
    }

    // FinaleProjectList를 반환하는 메서드
    private
    List<FinaleProjectList> getFinaleProjectList(int generationId) {
        return finaleProjectMapper.selectFinaleProjectList();
    }

    // ProjectUserInfo를 반환하는 메서드
    private
    List<ProjectUserInfo> getNoFinaleTeamUsers(int finaleProjectId) {
        return finaleProjectMapper.selectNoFinaleTeamUsers(finaleProjectId);
    }

    // FinaleTeamList를 반환하는 메서드
    private
    List<FinaleTeamList> getFinaleTeamList(int finaleProjectId) {
        return finaleProjectMapper.selectFinaleTeamList(finaleProjectId);
    }

    private void validateProjectCreate(FinaleProjectCreate request) {
        // 기본 필드 검증
        if (request.getTitle() == null || request.getTitle().trim().isEmpty()) {
            throw new BusinessException(ErrorCode.PROJECT_TITLE_REQUIRED);
        }

        if (request.getDescription() == null || request.getDescription().trim().isEmpty()) {
            throw new BusinessException(ErrorCode.PROJECT_DESCRIPTION_REQUIRED);
        }

        if (request.getUrl() == null || request.getUrl().trim().isEmpty()) {
            throw new BusinessException(ErrorCode.PROJECT_URL_REQUIRED);
        }

        // 팀 정보 검증
        if (request.getTeams() == null) {
            throw new BusinessException(ErrorCode.INVALID_TEAM_MEMBERS);
        }

        // 리더 검증
        if (request.getTeams().getLeaderId() <= 0) {
            throw new BusinessException(ErrorCode.PROJECT_LEADER_REQUIRED);
        }

        // 멤버 검증
        if (request.getTeams().getMemberIds() != null) {
            for (Integer memberId : request.getTeams().getMemberIds()) {
                if (memberId <= 0) {
                    throw new BusinessException(ErrorCode.INVALID_TEAM_MEMBERS,
                            "유효하지 않은 팀원 ID가 포함되어 있습니다.");
                }
            }
        }
    }

    private void validateUpdateRequest(int finaleProjectId, FinaleProjectUpdate request) {
        if (!finaleProjectMapper.checkFinaleProject(finaleProjectId)) {
            throw new BusinessException(ErrorCode.PROJECT_NOT_FOUND,
                    String.format("프로젝트 ID %d를 찾을 수 없습니다.", finaleProjectId));
        }

        if (request.getTitle() == null || request.getTitle().trim().isEmpty()) {
            throw new BusinessException(ErrorCode.PROJECT_TITLE_REQUIRED);
        }

        if (request.getDescription() == null || request.getDescription().trim().isEmpty()) {
            throw new BusinessException(ErrorCode.PROJECT_DESCRIPTION_REQUIRED);
        }

        if (request.getUrl() == null || request.getUrl().trim().isEmpty()) {
            throw new BusinessException(ErrorCode.PROJECT_URL_REQUIRED);
        }
    }

    private void validateProjectTeamUpdate(int finaleProjectId, FinaleProjectTeamUpdate request) {
        // 프로젝트와 팀 존재 여부 확인
        if (!finaleProjectMapper.checkFinaleProject(finaleProjectId)) {
            throw new BusinessException(ErrorCode.PROJECT_NOT_FOUND,
                    String.format("프로젝트 ID %d의 프로젝트를 찾을 수 없습니다.", finaleProjectId));
        }

        // 필수 필드 검증
        if (request.getTitle() == null || request.getTitle().trim().isEmpty()) {
            throw new BusinessException(ErrorCode.PROJECT_TITLE_REQUIRED);
        }

        if (request.getDescription() == null || request.getDescription().trim().isEmpty()) {
            throw new BusinessException(ErrorCode.PROJECT_DESCRIPTION_REQUIRED);
        }

        if (request.getUrl() == null || request.getUrl().trim().isEmpty()) {
            throw new BusinessException(ErrorCode.PROJECT_URL_REQUIRED);
        }
    }

    private void validateBoardDetail(int finaleProjectId, int boardId) {
        // 프로젝트 존재 여부 확인
        if (!finaleProjectMapper.checkFinaleProject(finaleProjectId)) {
            throw new BusinessException(ErrorCode.PROJECT_NOT_FOUND,
                    String.format("프로젝트를 찾을 수 없습니다. (프로젝트 ID: %d)", finaleProjectId));
        }

        // 게시글 존재 여부 확인
        if (!finaleProjectMapper.checkFinaleProjectBoard(finaleProjectId, boardId)) {
            throw new BusinessException(ErrorCode.BOARD_NOT_FOUND,
                    String.format("게시글을 찾을 수 없습니다. (프로젝트 ID: %d, 게시글 ID: %d)", finaleProjectId, boardId));
        }
    }

    private void validateBoardCreate(int finaleProjectId, FinaleProjectBoardRequest request, User currentUser) {
        // 프로젝트 존재 확인
        if (!finaleProjectMapper.checkFinaleProject(finaleProjectId)) {
            throw new BusinessException(ErrorCode.PROJECT_NOT_FOUND,
                    String.format("프로젝트를 찾을 수 없습니다. (프로젝트 ID: %d)", finaleProjectId));
        }

        // 팀 ID 조회
        int teamId = finaleProjectMapper.selectFinaleTeamId(finaleProjectId);

        // 팀 멤버 확인
        if (!finaleProjectMapper.checkTeamMember(teamId, currentUser.getUserId())) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED_TEAM_MEMBER,
                    "해당 팀의 멤버만 게시글을 작성할 수 있습니다.");
        }

        // 필수 필드 검증
        if (request.getTitle() == null || request.getTitle().trim().isEmpty()) {
            throw new BusinessException(ErrorCode.BOARD_TITLE_REQUIRED);
        }

        if (request.getContent() == null || request.getContent().trim().isEmpty()) {
            throw new BusinessException(ErrorCode.BOARD_CONTENT_REQUIRED);
        }
    }

    private void validateBoardUpdate(int finaleProjectId, int finaleProjectBoardId,
                                     FinaleProjectBoardRequest request, User currentUser) {
        // 프로젝트 존재 여부 확인
        if (!finaleProjectMapper.checkFinaleProject(finaleProjectId)) {
            throw new BusinessException(ErrorCode.PROJECT_NOT_FOUND);
        }

        // 게시글 작성자(팀 멤버) 확인
        if (!finaleProjectMapper.checkBoardTeamMember(finaleProjectId, finaleProjectBoardId, currentUser.getUserId())) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED_BOARD_UPDATE);
        }

        // 필수 필드 검증
        if (request.getTitle() == null || request.getTitle().trim().isEmpty()) {
            throw new BusinessException(ErrorCode.BOARD_TITLE_REQUIRED);
        }

        if (request.getContent() == null || request.getContent().trim().isEmpty()) {
            throw new BusinessException(ErrorCode.BOARD_CONTENT_REQUIRED);
        }
    }

}
