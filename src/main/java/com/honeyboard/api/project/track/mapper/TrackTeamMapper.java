package com.honeyboard.api.project.track.mapper;

import com.honeyboard.api.project.model.ProjectUserInfo;
import com.honeyboard.api.project.track.model.response.TrackTeamList;
import com.honeyboard.api.user.model.UserName;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TrackTeamMapper {

    int insertTrackTeam(@Param("trackProjectId") int trackProjectId);

    int getLastInsertedTeamId();

    int insertTeamLeader(@Param("teamId") int teamId, @Param("leaderId") int leaderId);

    int insertTeamMembers(@Param("teamId") int teamId, @Param("memberIds") List<Integer> memberIds);

    int existsByProjectIdAndUserId(@Param("trackProjectId") int trackProjectId, @Param("userId") int userId);

    int getTeamLeaderId(@Param("teamId") int teamId);

    int deleteAllTeamMembers(@Param("teamId") int teamId);

    int updateTeamLeader(@Param("teamId") int teamId, @Param("newLeaderId") int newLeaderId);

    int updateTeamCompleted(@Param("teamId") int teamId, @Param("isCompleted") boolean isCompleted);


    List<ProjectUserInfo> selectTrackProjectMembers(int trackProjectId);
}
