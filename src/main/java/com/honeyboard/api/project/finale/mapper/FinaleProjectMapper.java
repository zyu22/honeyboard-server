package com.honeyboard.api.project.finale.mapper;

import java.util.List;

import com.honeyboard.api.project.finale.model.FinaleProject;
import com.honeyboard.api.project.finale.model.FinaleTeam;
import com.honeyboard.api.user.model.User;

public interface FinaleProjectMapper {

	public List<FinaleTeam> selectSubmitStatusByDate(String targetDate);

	public List<User> selectRemainedUsers(int generationId);

	public int insertFinalProject(FinaleProject finaleProject);

	public int updateFinaleProject(FinaleProject finaleProject);

	public boolean removeFinalProject(int teamId);

}
