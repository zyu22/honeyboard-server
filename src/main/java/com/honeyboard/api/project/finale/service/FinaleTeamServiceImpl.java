package com.honeyboard.api.project.finale.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.honeyboard.api.project.finale.mapper.FinaleProjectTeamMapper;
import com.honeyboard.api.project.finale.model.FinaleProject;
import com.honeyboard.api.project.finale.model.FinaleTeam;
import com.honeyboard.api.user.model.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FinaleTeamServiceImpl implements FinaleTeamService {

	private final FinaleProjectTeamMapper finaleProjectMapper;

	@Override
	public List<FinaleTeam> findStatusByDate(String targetDate) {
		List<FinaleTeam> teams = finaleProjectMapper.selectSubmitStatusByDate(targetDate);
		for (FinaleTeam team : teams) {
			team.setMembers(finaleProjectMapper.selectTeamMembers(team.getTeamId()));
		}

		return teams;
	}

	@Override
	public List<User> getRemainedUsers(int generationId) {
		return finaleProjectMapper.selectRemainedUsers(generationId);
	}

	@Override
	public boolean saveFinaleProject(FinaleProject finaleProject) {
		return finaleProjectMapper.insertFinaleProject(finaleProject) > 0;
	}

	@Override
	public boolean updateFinaleProject(FinaleProject finaleProject) {
		return finaleProjectMapper.updateFinaleProject(finaleProject) > 0;
	}

	@Override
	public boolean removeFinaleProject(int teamId) {
		return finaleProjectMapper.removeFinaleProject(teamId);
	}

}
