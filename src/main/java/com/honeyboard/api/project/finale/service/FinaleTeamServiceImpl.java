package com.honeyboard.api.project.finale.service;

import com.honeyboard.api.project.finale.mapper.FinaleTeamMapper;
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

}
