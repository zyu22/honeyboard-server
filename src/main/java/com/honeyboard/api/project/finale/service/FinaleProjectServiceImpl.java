package com.honeyboard.api.project.finale.service;

import java.util.List;

import com.honeyboard.api.exception.BusinessException;
import com.honeyboard.api.exception.ErrorCode;
import com.honeyboard.api.project.finale.mapper.FinaleProjectBoardMapper;
import com.honeyboard.api.project.finale.mapper.FinaleProjectMapper;
import com.honeyboard.api.project.finale.mapper.FinaleTeamMapper;
import com.honeyboard.api.project.finale.model.request.FinaleProjectCreate;
import com.honeyboard.api.project.finale.model.request.FinaleProjectUpdate;
import com.honeyboard.api.project.finale.model.response.*;
import com.honeyboard.api.project.model.ProjectUserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FinaleProjectServiceImpl implements FinaleProjectService {

    private final FinaleProjectMapper finaleProjectMapper;
    private final FinaleTeamMapper finaleTeamMapper;
    private final FinaleProjectBoardMapper finaleProjectBoardMapper;

    @Override
    public FinaleProjectResponse getFinaleResponse(int generationId) {
        log.info("FinaleResponse 가져오기 시작 generationId: {}", generationId);
        FinaleProjectResponse fpr = new FinaleProjectResponse();
        List<FinaleProjectList> projects = getFinaleProjectList(generationId);
        log.info("FinaleResponse 가져오기 성공");
        if (!projects.isEmpty()) {
            int finaleProjectId = projects.get(0).getId();
            log.info("FinaleProjectId : {}", finaleProjectId);
        }
        fpr.setProjects(projects);
        fpr.setNoTeamUsers(getNoFinaleTeamUsers(generationId));
        fpr.setTeams(finaleTeamMapper.selectFinaleTeamList(generationId));
        return fpr;
    }

    @Override
    public int createFinaleProject(FinaleProjectCreate request, int generationId, int userId) {
        // 팀 생성
        finaleProjectMapper.insertTeam(generationId);
        int teamId = finaleProjectMapper.selectLastInsertedId();
        request.setTeamId(teamId);

        // 프로젝트 생성
        finaleProjectMapper.insertProject(request, userId);
        int projectId = finaleProjectMapper.selectLastInsertedId();

        // 팀 리더 생성
        finaleProjectMapper.insertTeamLeader(teamId, request.getTeams().getLeaderId());

        // 팀 멤버들 생성
        for (Integer memberId : request.getTeams().getMemberIds()) {
            finaleProjectMapper.insertTeamMember(teamId, memberId);
        }

        return projectId;
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
    public boolean deleteFinaleProject(int finaleProjectId, int finaleTeamId) {
        log.info("프로젝트 및 팀 삭제 시작 - finaleProjectId: {}, finaleTeamId: {}", finaleProjectId, finaleTeamId);
        if (!finaleProjectMapper.checkFinaleProject(finaleProjectId)) {
            throw new BusinessException(ErrorCode.PROJECT_NOT_FOUND,
                    String.format("프로젝트 ID %d를 찾을 수 없습니다.", finaleProjectId));
        }

        int result = finaleProjectMapper.deleteFinaleProject(finaleProjectId);
        if (result <= 0) {
            log.error("프로젝트 삭제 실패 - finaleProjectId: {}", finaleProjectId);
            throw new BusinessException(ErrorCode.PROJECT_DELETE_FAILED);
        }

        result = finaleTeamMapper.deleteTeam(finaleTeamId);
        if (result <= 0) {
            log.error("팀 삭제 실패 - finaleTeamId: {}", finaleTeamId);
            throw new BusinessException(ErrorCode.TEAM_DELETE_FAILED);
        }

        log.info("프로젝트 및 팀 삭제 완료 - finaleProjectId: {}", finaleProjectId);
        return result > 0;
    }

    @Override
    @Transactional(readOnly = true)
    public FinaleProjectDetail getFinaleProjectDetail(int finaleProjectId) {
        log.info("프로젝트 상세 조회 시작 - finaleProjectId: {}", finaleProjectId);
        if (!finaleProjectMapper.checkFinaleProject(finaleProjectId)) {
            throw new BusinessException(ErrorCode.PROJECT_NOT_FOUND,
                    String.format("프로젝트 ID %d를 찾을 수 없습니다.", finaleProjectId));
        }
        FinaleProjectDetail projectDetail = finaleProjectMapper.selectFinaleProjectDetail(finaleProjectId);
        projectDetail.setMembers(finaleTeamMapper.selectFinaleProjectDetailMembers(projectDetail.getFinaleTeamId()));
        projectDetail.setBoards(finaleProjectBoardMapper.selectFinaleProjectDetailBoards(finaleProjectId));
        log.info("프로젝트 상세 조회 완료 - finaleProjectId: {}", finaleProjectId);
        return projectDetail;
    }

    private List<FinaleProjectList> getFinaleProjectList(int generationId) {
        return finaleProjectMapper.selectFinaleProjectList(generationId);
    }

    private List<ProjectUserInfo> getNoFinaleTeamUsers(int generationId) {
        return finaleTeamMapper.selectNoFinaleTeamUsersByGeneration(generationId);
    }

    private List<FinaleTeamList> getFinaleTeamList(int generationId) {
        return finaleTeamMapper.selectFinaleTeamList(generationId);
    }

    private void validateProjectCreate(FinaleProjectCreate request) {
        if (request.getTitle() == null || request.getTitle().trim().isEmpty()) {
            throw new BusinessException(ErrorCode.PROJECT_TITLE_REQUIRED);
        }
        if (request.getDescription() == null || request.getDescription().trim().isEmpty()) {
            throw new BusinessException(ErrorCode.PROJECT_DESCRIPTION_REQUIRED);
        }
        if (request.getUrl() == null || request.getUrl().trim().isEmpty()) {
            throw new BusinessException(ErrorCode.PROJECT_URL_REQUIRED);
        }
        if (request.getTeams() == null) {
            throw new BusinessException(ErrorCode.INVALID_TEAM_MEMBERS);
        }
        if (request.getTeams().getLeaderId() <= 0) {
            throw new BusinessException(ErrorCode.PROJECT_LEADER_REQUIRED);
        }
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

}
