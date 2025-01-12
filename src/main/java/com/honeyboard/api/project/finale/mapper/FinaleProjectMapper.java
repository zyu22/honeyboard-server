package com.honeyboard.api.project.finale.mapper;

import com.honeyboard.api.project.finale.model.request.FinaleProjectBoardRequest;
import com.honeyboard.api.project.finale.model.request.FinaleProjectCreate;
import com.honeyboard.api.project.finale.model.request.FinaleProjectTeamUpdate;
import com.honeyboard.api.project.finale.model.request.FinaleProjectUpdate;
import com.honeyboard.api.project.finale.model.response.*;
import com.honeyboard.api.project.model.ProjectUserInfo;
import com.honeyboard.api.project.model.TeamMemberInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public
interface FinaleProjectMapper {

    // 프로젝트 리스트 조회
    List<FinaleProjectList> selectFinaleProjectList();

    // 팀이 없는 유저 조회
    List<ProjectUserInfo> selectNoFinaleTeamUsers(int finaleProjectId);

    // 팀 리스트 조회
    List<FinaleTeamList> selectFinaleTeamList(int finaleProjectId);

    // 프로젝트 생성
    int insertFinaleProject(FinaleProjectCreate project);

    // 프로젝트 수정
    int updateFinaleProject(@Param("finaleProjectId") int finaleProjectId,
                            @Param("request") FinaleProjectUpdate request);

    // 프로젝트 존재 확인
    boolean checkFinaleProject(@Param("finaleProjectId") int finaleProjectId);

    // 프로젝트 삭제
    int updateFinaleProjectDeleteStatus(int finaleProjectId);

    // 프로젝트 상세 조회
    FinaleProjectDetail selectFinaleProjectDetail(int finaleProjectId);

    // 프로젝트 상세 팀 인원 조회
    List<TeamMemberInfo> selectFinaleProjectDetailMembers(int finaleTeamId);

    // 프로젝트 상세 내 게시글 조회
    List<FinaleProjectBoardList> selectFinaleProjectDetailBoards(int finaleProjectId);

    int updateFinaleProjectTeam(@Param("finaleProjectId") int finaleProjectId,
                                @Param("finaleTeamId") int finaleTeamId,
                                @Param("request") FinaleProjectTeamUpdate request);

    FinaleProjectBoardDetail selectFinaleProjectBoardDetail(
            @Param("finaleProjectId") int finaleProjectId,
            @Param("boardId") int boardId
    );

    boolean checkFinaleProjectBoard(@Param("finaleProjectId") int finaleProjectId,
                                    @Param("boardId") int boardId);

    int selectFinaleTeamId(int finaleProjectId);

    boolean checkTeamMember(@Param("teamId") int teamId,
                            @Param("userId") int userId);

    int insertFinaleProjectBoard(@Param("finaleProjectId") int finaleProjectId,
                                 @Param("request") FinaleProjectBoardRequest request);

}
