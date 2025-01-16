package com.honeyboard.api.project.finale.service;

import com.honeyboard.api.exception.BusinessException;
import com.honeyboard.api.exception.ErrorCode;
import com.honeyboard.api.project.finale.mapper.FinaleProjectMapper;
import com.honeyboard.api.project.finale.mapper.FinaleTeamMapper;

import com.honeyboard.api.project.finale.model.request.FinaleProjectTeamUpdate;
import com.honeyboard.api.project.finale.model.response.FinaleTeamList;
import com.honeyboard.api.project.model.ProjectUserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FinaleTeamServiceImpl implements FinaleTeamService {

    private final FinaleTeamMapper finaleTeamMapper;
    private final FinaleProjectMapper finaleProjectMapper;

    @Override
    public List<ProjectUserInfo> getNoFinaleTeamUsers(int finaleProjectId) {
        log.info("팀이 없는 유저 조회 시작 - finaleProjectId: {}", finaleProjectId);
        return finaleTeamMapper.selectNoFinaleTeamUsers(finaleProjectId);
    }

    @Override
    public List<FinaleTeamList> getFinaleTeamList(int finaleProjectId) {
        log.info("팀 리스트 조회 시작 - finaleProjectId: {}", finaleProjectId);
        return finaleTeamMapper.selectFinaleTeamList(finaleProjectId);
    }

    @Override
    @Transactional
    public boolean updateFinaleProjectTeam(int finaleProjectId, int finaleTeamId, FinaleProjectTeamUpdate request) {
        log.info("팀 프로젝트 수정 시작 - finaleProjectId: {}, finaleTeamId: {}", finaleProjectId, finaleTeamId);

        validateProjectTeamUpdate(finaleProjectId, request);

        int result = finaleTeamMapper.updateFinaleProjectTeam(finaleProjectId, finaleTeamId, request);
        if (result <= 0) {
            throw new BusinessException(ErrorCode.PROJECT_TEAM_UPDATE_FAILED);
        }

        log.info("팀 프로젝트 수정 완료 - finaleProjectId: {}, finaleTeamId: {}", finaleProjectId, finaleTeamId);
        return true;
    }

    private void validateProjectTeamUpdate(int finaleProjectId, FinaleProjectTeamUpdate request) {
        if (!finaleProjectMapper.checkFinaleProject(finaleProjectId)) {
            throw new BusinessException(ErrorCode.PROJECT_NOT_FOUND,
                    String.format("프로젝트 ID %d의 프로젝트를 찾을 수 없습니다.", finaleProjectId));
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

