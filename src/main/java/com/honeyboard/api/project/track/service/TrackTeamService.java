package com.honeyboard.api.project.track.service;

import com.honeyboard.api.project.track.model.TrackProjectStatus;
import com.honeyboard.api.project.track.model.TrackTeam;

public interface TrackTeamService {

    TrackProjectStatus getProjectStatus(int generationId, int projectId);

    void addTrackTeam(TrackTeam trackTeam);

    void updateTrackTeam(TrackTeam trackTeam);

    void removeTrackTeam(int teamId);
}
