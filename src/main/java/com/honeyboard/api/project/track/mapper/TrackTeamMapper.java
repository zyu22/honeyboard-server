package com.honeyboard.api.project.track.mapper;

import com.honeyboard.api.project.track.model.TrackProjectStatus;
import com.honeyboard.api.project.track.model.TrackTeam;
import com.honeyboard.api.project.track.model.TrackTeamMember;

import java.util.List;
import java.util.Map;

public interface TrackTeamMapper {

    TrackProjectStatus selectTrackProjectStatus(int generationId, int projectId);

    int insertTrackTeam(TrackTeam trackTeam);

    int insertTrackTeamMember(Map<String, Object> members);

    int updateTrackTeamMembers(List<TrackTeamMember> members);

    int deleteTrackTeam(int teamId);
}
