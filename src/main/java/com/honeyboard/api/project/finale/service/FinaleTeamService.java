package com.honeyboard.api.project.finale.service;

import com.honeyboard.api.project.finale.model.request.FinaleProjectTeamUpdate;
import com.honeyboard.api.project.finale.model.response.FinaleTeamList;
import com.honeyboard.api.project.model.ProjectUserInfo;
import com.honeyboard.api.user.model.UserName;

import java.time.LocalDate;
import java.util.List;

public
interface FinaleTeamService {

    List<ProjectUserInfo> getNoFinaleTeamUsers(int finaleProjectId);

    List<FinaleTeamList> getFinaleTeamList(int finaleProjectId);

    boolean updateFinaleProjectTeam(int finaleProjectId, int finaleTeamId, FinaleProjectTeamUpdate request);

}
