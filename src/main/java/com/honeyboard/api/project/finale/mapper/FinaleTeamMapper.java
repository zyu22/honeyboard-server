package com.honeyboard.api.project.finale.mapper;

import java.util.List;

import com.honeyboard.api.project.finale.model.request.FinaleProjectTeamUpdate;
import com.honeyboard.api.project.finale.model.response.FinaleTeamList;
import com.honeyboard.api.project.model.ProjectUserInfo;
import com.honeyboard.api.project.model.TeamMemberInfo;
import com.honeyboard.api.project.model.TeamRequest;
import org.apache.ibatis.annotations.Param;

public interface FinaleTeamMapper {

    // 팀이 없는 유저 조회
    List<ProjectUserInfo> selectNoFinaleTeamUsers(int finaleProjectId);

    // 팀 없는 사람
    List<ProjectUserInfo> selectNoTeamFinaleTeamUsers();

    // 팀 리스트 조회
    List<FinaleTeamList> selectFinaleTeamList();

    // 프로젝트 상세 팀 인원 조회
    List<TeamMemberInfo> selectFinaleProjectDetailMembers(int finaleTeamId);

    int selectFinaleTeamId(int finaleProjectId);

    int updateTeamLeader(TeamRequest request);
    int deleteTeamMembers(TeamRequest request);
    int insertTeamMember(TeamRequest request);

    boolean checkTeamMember(@Param("teamId") int teamId,
                            @Param("userId") int userId);

    boolean checkBoardTeamMember(@Param("finaleProjectId") int finaleProjectId,
                                 @Param("finaleProjectBoardId") int finaleProjectBoardId,
                                 @Param("userId") int userId);

}
