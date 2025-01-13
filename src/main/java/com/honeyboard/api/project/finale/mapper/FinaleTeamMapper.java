package com.honeyboard.api.project.finale.mapper;

import java.util.List;

import com.honeyboard.api.project.finale.model.request.FinaleProjectTeamUpdate;
import com.honeyboard.api.project.finale.model.response.FinaleTeamList;
import com.honeyboard.api.project.model.ProjectUserInfo;
import com.honeyboard.api.project.model.TeamMemberInfo;
import org.apache.ibatis.annotations.Param;

public interface FinaleTeamMapper {

    // 팀이 없는 유저 조회
    List<ProjectUserInfo> selectNoFinaleTeamUsers(int finaleProjectId);

    // 팀 리스트 조회
    List<FinaleTeamList> selectFinaleTeamList(int finaleProjectId);

    // 프로젝트 상세 팀 인원 조회
    List<TeamMemberInfo> selectFinaleProjectDetailMembers(int finaleTeamId);

    int selectFinaleTeamId(int finaleProjectId);

    int updateFinaleProjectTeam(@Param("finaleProjectId") int finaleProjectId,
                                @Param("finaleTeamId") int finaleTeamId,
                                @Param("request") FinaleProjectTeamUpdate request);

    boolean checkTeamMember(@Param("teamId") int teamId,
                            @Param("userId") int userId);

    boolean checkBoardTeamMember(@Param("finaleProjectId") int finaleProjectId,
                                 @Param("finaleProjectBoardId") int finaleProjectBoardId,
                                 @Param("userId") int userId);

}
