package com.honeyboard.api.project.finale.service;

import com.honeyboard.api.project.finale.mapper.FinaleTeamMapper;
import com.honeyboard.api.project.finale.model.FinaleTeamRequest;
import com.honeyboard.api.project.finale.model.FinaleTeam;
import com.honeyboard.api.user.model.UserName;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

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
    public FinaleTeam createTeam(FinaleTeamRequest team) {
        validateTeamRequest(team);

        FinaleTeam newTeam = new FinaleTeam();
        team.setGenerationId(team.getGenerationId());

        try {
            // 팀 생성
            finaleTeamMapper.insertFinaleTeam(newTeam);

            // 팀장 등록
            finaleTeamMapper.insertFinaleTeamMember(
                    newTeam.getTeamId(),
                    team.getLeaderId(),
                    "leader"
            );

            // 팀원들 등록
            for (Integer memberId : team.getMemberIds()) {
                finaleTeamMapper.insertFinaleTeamMember(
                        newTeam.getTeamId(),
                        memberId,
                        "member"
                );
            }

            return newTeam;
        } catch (Exception e) {
            log.error("팀 생성 실패: {}", e.getMessage());
            throw new RuntimeException("팀 생성에 실패했습니다.", e);
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
