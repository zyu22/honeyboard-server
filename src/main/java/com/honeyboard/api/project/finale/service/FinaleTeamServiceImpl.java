package com.honeyboard.api.project.finale.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.honeyboard.api.project.finale.mapper.FinaleProjectMapper;
import com.honeyboard.api.project.finale.model.FinaleProject;
import com.honeyboard.api.project.finale.model.FinaleTeam;
import com.honeyboard.api.user.model.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FinaleTeamServiceImpl implements FinaleTeamService {

	private final FinaleProjectMapper finaleProjectMapper;

	@Override
	public List<FinaleTeam> findStatusByDate(String targetDate) {
		return finaleProjectMapper.selectSubmitStatusByDate(targetDate);
	}

	@Override
	public List<User> getRemainedUsers(int generationId) {
		return finaleProjectMapper.selectRemainedUsers(generationId);
	}

	@Override
	public boolean saveFinaleProject(FinaleProject finaleProject) {
		return finaleProjectMapper.insertFinalProject(finaleProject) > 0;
	}

	@Override
	public boolean updateFinaleProject(FinaleProject finaleProject) {
		return finaleProjectMapper.updateFinaleProject(finaleProject) > 0;
	}

	@Override
	public boolean removeFinalProject(int teamId) {
		return finaleProjectMapper.removeFinalProject(teamId);
	}

}
