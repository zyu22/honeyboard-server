package com.honeyboard.api.project.finale.service;

import com.honeyboard.api.project.finale.mapper.FinaleTeamMapper;
import com.honeyboard.api.project.finale.model.FinaleMember;
import com.honeyboard.api.project.finale.model.FinaleTeamRequest;
import com.honeyboard.api.project.finale.model.FinaleTeam;
import com.honeyboard.api.user.model.UserName;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public
class FinaleTeamServiceImpl implements FinaleTeamService {

    private final FinaleTeamMapper finaleTeamMapper;

    @Override
    public List<FinaleTeam> findStatusByDate(LocalDate targetDate) {
        if (targetDate == null) {
            throw new IllegalArgumentException("조회할 날짜를 입력해주세요.");
        }
        if (targetDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("미래 날짜는 조회할 수 없습니다.");
        }

        try {
            return finaleTeamMapper.selectSubmitStatusByDate(targetDate);
        } catch (Exception e) {
            log.error("팀 상태 조회 실패 - 날짜: {}, 오류: {}", targetDate, e.getMessage());
            throw new RuntimeException("팀 상태 조회에 실패했습니다.", e);
        }
    }

    @Override
    public List<UserName> getRemainedUsers(int generationId) {
        if (generationId <= 0) {
            throw new IllegalArgumentException("유효하지 않은 기수 ID입니다.");
        }

        try {
            return finaleTeamMapper.selectRemainedUsers(generationId);
        } catch (Exception e) {
            log.error("미배정 사용자 조회 실패 - 기수: {}, 오류: {}", generationId, e.getMessage());
            throw new RuntimeException("미배정 사용자 조회에 실패했습니다.", e);
        }
    }

    @Override
    @Transactional
    public void createTeam(FinaleTeamRequest request) {
        validateTeamRequest(request);

        try {
            FinaleTeam newTeam = new FinaleTeam();
            newTeam.setGenerationId(request.getGenerationId());

            List<Integer> memberIds = request.getMemberIds().stream()
                    .filter(id -> !id.equals(request.getLeaderId()))
                    .collect(Collectors.toList());

            finaleTeamMapper.insertFinaleTeamWithMembers(newTeam, request.getLeaderId(), memberIds);
        } catch (Exception e) {
            log.error("팀 생성 실패: {}", e.getMessage());
            throw new RuntimeException("팀 생성에 실패했습니다.", e);
        }
    }

    @Override
    @Transactional
    public void updateTeam(FinaleTeamRequest request) {
        validateTeamRequest(request);

        if (!finaleTeamMapper.existsTeamById(request.getTeamId())) {
            throw new IllegalArgumentException("존재하지 않는 팀입니다.");
        }

        try {
            List<Integer> memberIds = request.getMemberIds().stream()
                    .filter(id -> !id.equals(request.getLeaderId()))
                    .collect(Collectors.toList());

            finaleTeamMapper.updateTeamMembers(
                    request.getTeamId(),
                    request.getLeaderId(),
                    memberIds
            );
        } catch (Exception e) {
            log.error("팀 수정 실패 - 팀 ID: {}, 오류: {}", request.getTeamId(), e.getMessage());
            throw new RuntimeException("팀 수정에 실패했습니다.", e);
        }
    }



    private void validateTeamRequest(FinaleTeamRequest team) {
        if (team.getGenerationId() == null || team.getGenerationId() <= 0) {
            throw new IllegalArgumentException("유효하지 않은 기수 ID입니다.");
        }
        if (team.getLeaderId() == null || team.getLeaderId() <= 0) {
            throw new IllegalArgumentException("팀장 정보가 필요합니다.");
        }
        if (team.getMemberIds() == null || team.getMemberIds().isEmpty()) {
            throw new IllegalArgumentException("최소 1명 이상의 팀원이 필요합니다.");
        }
    }

}
