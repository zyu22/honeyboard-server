package com.honeyboard.api.project.finale.mapper;

import java.time.LocalDate;
import java.util.List;

import com.honeyboard.api.project.finale.model.FinaleMember;
import com.honeyboard.api.project.finale.model.FinaleTeam;
import com.honeyboard.api.user.model.UserName;
import org.apache.ibatis.annotations.Param;

public interface FinaleTeamMapper {

	List<FinaleTeam> selectSubmitStatusByDate(LocalDate targetDate);

	List<UserName> selectRemainedUsers(int generationId);

	List<FinaleMember> selectTeamMembers(int teamId);

	FinaleTeam selectTeamById(int teamId);

	int insertFinaleTeam(FinaleTeam team);

	boolean existsTeamById(Integer teamId);

	int deleteTeamMembers(Integer teamId);
	int insertTeamMember(
			@Param("teamId") Integer teamId,
			@Param("userId") Integer userId,
			@Param("role") String role
	);

	int deleteTeamMember(
			@Param("teamId") Integer teamId,
			@Param("userId") Integer userId
	);

	int insertFinaleTeamMember(
			@Param("teamId") Integer teamId,
			@Param("userId") Integer userId,
			@Param("role") String role
	);
}
