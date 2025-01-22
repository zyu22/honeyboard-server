package com.honeyboard.api.project.track.service;

import com.honeyboard.api.project.model.TeamRequest;
import com.honeyboard.api.project.track.model.response.TrackTeamList;
import com.honeyboard.api.user.model.UserName;

import java.util.List;

public interface TrackTeamService {

    void addTrackTeam(int trackProjectId, TeamRequest trackTeam);

    void updateTrackTeam(int trackProjectId, int trackTeamId, TeamRequest trackTeam);
}
