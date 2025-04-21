package com.honeyboard.api.project.finale.service;

import com.honeyboard.api.exception.BusinessException;
import com.honeyboard.api.exception.ErrorCode;
import com.honeyboard.api.project.finale.mapper.FinaleProjectMapper;
import com.honeyboard.api.project.finale.mapper.FinaleTeamMapper;

import com.honeyboard.api.project.finale.model.request.FinaleProjectTeamUpdate;
import com.honeyboard.api.project.finale.model.response.FinaleTeamList;
import com.honeyboard.api.project.model.ProjectUserInfo;
import com.honeyboard.api.project.model.TeamRequest;
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
    public List<ProjectUserInfo> getFinaleTeamUsers() {
        log.info("팀 생성 시 팀 안된 유저 조회");
        return finaleTeamMapper.selectNoTeamFinaleTeamUsers();
    }


    @Override
    public List<FinaleTeamList> getFinaleTeamList(int generationId) {
        log.info("팀 리스트 조회 시작 - finaleProjectId: {}", generationId);
        return finaleTeamMapper.selectFinaleTeamList(generationId);
    }

    @Override
    @Transactional
    public boolean updateFinaleProjectTeam(TeamRequest request) {
        log.info("팀 프로젝트 수정 시작 - request: {}", request.toString());

        validateProjectTeamUpdate(request);

        int result = finaleTeamMapper.deleteTeamMembers(request)
                + finaleTeamMapper.updateTeamLeader(request)
                + finaleTeamMapper.insertTeamMember(request);
        if (result < 3) {
            throw new BusinessException(ErrorCode.PROJECT_TEAM_UPDATE_FAILED);
        }

        log.info("팀 프로젝트 수정 완료 - request: {}", request.toString());
        return true;
    }

    private void validateProjectTeamUpdate(TeamRequest request) {
        if (request.getId() <= 0) {
            throw new BusinessException(ErrorCode.PROJECT_TEAM_UPDATE_FAILED);
        }

        if (request.getLeaderId() <= 0) {
            throw new BusinessException(ErrorCode.PROJECT_TEAM_UPDATE_FAILED);
        }
    }
}

