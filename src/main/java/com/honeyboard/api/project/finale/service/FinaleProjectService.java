package com.honeyboard.api.project.finale.service;

import com.honeyboard.api.project.finale.model.request.FinaleProjectCreate;
import com.honeyboard.api.project.finale.model.request.FinaleProjectUpdate;
import com.honeyboard.api.project.finale.model.response.FinaleProjectDetail;
import com.honeyboard.api.project.finale.model.response.FinaleProjectResponse;

public interface FinaleProjectService {

    FinaleProjectResponse getFinaleResponse(int generationId);

    int createFinaleProject(FinaleProjectCreate request, int generationId, int userId);

    boolean updateFinaleProject(int finaleProjectId, FinaleProjectUpdate request);

    boolean deleteFinaleProject(int finaleProjectId, int finaleTeamId);

    FinaleProjectDetail getFinaleProjectDetail(int finaleProjectId);

}
