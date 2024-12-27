package com.honeyboard.api.project.finale.mapper;

import java.util.List;

import com.honeyboard.api.project.finale.model.FinaleMember;
import com.honeyboard.api.project.finale.model.FinaleProject;
import com.honeyboard.api.project.finale.model.FinaleTeam;
import com.honeyboard.api.user.model.User;

public interface FinaleProjectTeamMapper {

	public List<FinaleTeam> selectSubmitStatusByDate(String targetDate);

	public List<User> selectRemainedUsers(int generationId);

	public int insertFinaleProject(FinaleProject finaleProject);

	public int updateFinaleProject(FinaleProject finaleProject);

	public boolean removeFinaleProject(int teamId);

	public List<FinaleMember> selectTeamMembers(int teamId);

}
