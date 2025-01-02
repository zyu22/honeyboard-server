package com.honeyboard.api.project.finale.mapper;

import java.time.LocalDate;
import java.util.List;

import com.honeyboard.api.project.finale.model.FinaleMember;
import com.honeyboard.api.project.finale.model.FinaleProject;
import com.honeyboard.api.project.finale.model.FinaleTeam;
import com.honeyboard.api.user.model.UserName;

public interface FinaleTeamMapper {

	public List<FinaleTeam> selectSubmitStatusByDate(LocalDate targetDate);

	public List<UserName> selectRemainedUsers(int generationId);

	public List<FinaleMember> selectTeamMembers(int teamId);

}
