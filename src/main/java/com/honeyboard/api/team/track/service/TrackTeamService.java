package com.honeyboard.api.team.track.service;

import com.honeyboard.api.team.track.model.TrackTeam;

public interface TrackTeamService {

	void addTeam(int projectId, int generationId);

	boolean updateTeam(int projectId, TrackTeam team);

	boolean softDeleteTema(int projectId, int teamId);

}
