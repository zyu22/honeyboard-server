package com.honeyboard.api.project.finale.service;

import com.honeyboard.api.project.finale.model.FinaleTeam;
import com.honeyboard.api.user.model.User;

import java.time.LocalDate;
import java.util.List;

public
interface FinaleTeamService {
    List<FinaleTeam> findStatusByDate(LocalDate targetDate);

    List<User> getRemainedUsers(int generationId);
}
