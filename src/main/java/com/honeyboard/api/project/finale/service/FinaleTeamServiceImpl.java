package com.honeyboard.api.project.finale.service;

import com.honeyboard.api.project.finale.mapper.FinaleTeamMapper;
import com.honeyboard.api.project.finale.model.FinaleTeam;
import com.honeyboard.api.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public
class FinaleTeamServiceImpl implements FinaleTeamService {

    private
    FinaleTeamMapper finaleTeamMapper;

    @Override
    public List<FinaleTeam> findStatusByDate(LocalDate targetDate) {
        List<FinaleTeam> teams = finaleTeamMapper.selectSubmitStatusByDate(targetDate);

        return teams;
    }

    @Override
    public List<User> getRemainedUsers(int generationId) {
        return finaleTeamMapper.selectRemainedUsers(generationId);
    }
}
