package com.honeyboard.api.project.track.mapper;

import com.honeyboard.api.project.track.model.TrackProjectStatus;
import com.honeyboard.api.project.track.model.TrackTeam;
import com.honeyboard.api.project.track.model.TrackTeamMember;
import com.honeyboard.api.user.model.UserName;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TrackTeamMapper {

    TrackProjectStatus selectTrackProjectStatus(int generationId, int projectId);

    int insertTrackTeam(TrackTeam trackTeam);

    int insertTrackTeamMember(TrackTeam trackTeam);

    int updateTrackTeamMembers(List<TrackTeamMember> members);

    int deleteTrackTeam(int teamId);

    List<UserName> selectRemainingUsers(@Param("generationId")Integer generationId, @Param("projectId") int projectId);
}
