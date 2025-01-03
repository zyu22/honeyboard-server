package com.honeyboard.api.project.finale.service;

import com.honeyboard.api.project.finale.model.FinaleTeamRequest;
import com.honeyboard.api.project.finale.model.FinaleTeam;
import com.honeyboard.api.user.model.UserName;

import java.time.LocalDate;
import java.util.List;

public
interface FinaleTeamService {
    List<FinaleTeam> findStatusByDate(LocalDate targetDate);

    List<UserName> getRemainedUsers(int generationId);

    FinaleTeam createTeam(FinaleTeamRequest finaleTeam);

    FinaleTeam updateTeam(FinaleTeamRequest request);
}
