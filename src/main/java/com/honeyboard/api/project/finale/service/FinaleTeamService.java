package com.honeyboard.api.project.finale.service;

import java.util.List;

import com.honeyboard.api.project.finale.model.FinaleProject;
import com.honeyboard.api.project.finale.model.FinaleTeam;
import com.honeyboard.api.user.model.User;

public interface FinaleTeamService {

	List<FinaleTeam> findStatusByDate(String targetDate);

	List<User> getRemainedUsers(int generationId);

	boolean saveFinaleProject(FinaleProject finaleProject);

	boolean updateFinaleProject(FinaleProject finaleProject);

	boolean removeFinaleProject(int teamId);

}
