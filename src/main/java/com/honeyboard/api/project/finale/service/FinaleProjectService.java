package com.honeyboard.api.project.finale.service;

import com.honeyboard.api.project.finale.model.request.FinaleProjectBoardRequest;
import com.honeyboard.api.project.finale.model.request.FinaleProjectCreate;
import com.honeyboard.api.project.finale.model.request.FinaleProjectTeamUpdate;
import com.honeyboard.api.project.finale.model.request.FinaleProjectUpdate;
import com.honeyboard.api.project.finale.model.response.*;
import com.honeyboard.api.project.model.ProjectUserInfo;
import com.honeyboard.api.project.model.TeamMemberInfo;
import com.honeyboard.api.user.model.User;

import java.util.List;

public interface FinaleProjectService {

    FinaleProjectResponse getFinaleResponse(int generationId);

    int createFinaleProject(FinaleProjectCreate request);

    boolean updateFinaleProject(int finaleProjectId, FinaleProjectUpdate request);

    boolean deleteFinaleProject(int finaleProjectId);

    FinaleProjectDetail getFinaleProjectDetail(int finaleProjectId);

    boolean updateFinaleProjectTeam(int finaleProjectId, int finaleTeamId, FinaleProjectTeamUpdate request);

    FinaleProjectBoardDetail getFinaleProjectBoardDetail(int finaleProjectId, int boardId);

    int createFinaleProjectBoard(int finaleProjectId, FinaleProjectBoardRequest request, User currentUser);

}
