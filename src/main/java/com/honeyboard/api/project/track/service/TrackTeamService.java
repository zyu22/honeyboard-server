package com.honeyboard.api.project.track.service;

import com.honeyboard.api.project.track.model.TrackProjectStatus;
import com.honeyboard.api.project.track.model.TrackTeam;
import com.honeyboard.api.user.model.UserName;

import java.util.List;

public interface TrackTeamService {

    TrackProjectStatus getProjectStatus(int generationId, int projectId);

    void addTrackTeam(TrackTeam trackTeam);

    void updateTrackTeam(TrackTeam trackTeam);

    void removeTrackTeam(int teamId);

    List<UserName> getRemainedUsers(Integer generationId, int projectId);
}
