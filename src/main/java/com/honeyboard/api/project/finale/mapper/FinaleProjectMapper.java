package com.honeyboard.api.project.finale.mapper;

import com.honeyboard.api.project.finale.model.request.FinaleProjectCreate;
import com.honeyboard.api.project.finale.model.request.FinaleProjectUpdate;
import com.honeyboard.api.project.finale.model.response.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public
interface FinaleProjectMapper {

    // 프로젝트 리스트 조회
    List<FinaleProjectList> selectFinaleProjectList(@Param("generationId") int generationId);

    // 프로젝트 생성
    int insertProject(@Param("finaleProjectCreate") FinaleProjectCreate project, @Param("userId") int userId);

    // 팀 생성
    int insertTeam(@Param("generationId") int generationId);

    // 팀 리더 생성
    int insertTeamLeader(@Param("teamId") int teamId, @Param("leaderId") int leaderId);

    // 팀 멤버 생성
    int insertTeamMember(@Param("teamId") int teamId, @Param("memberId") int memberId);

    // 프로젝트 수정
    int updateFinaleProject(@Param("finaleProjectId") int finaleProjectId,
                            @Param("request") FinaleProjectUpdate request);

    // 프로젝트 존재 확인
    boolean checkFinaleProject(@Param("finaleProjectId") int finaleProjectId);

    // 프로젝트 삭제
    int deleteFinaleProject(int finaleProjectId);

    // 프로젝트 상세 조회
    FinaleProjectDetail selectFinaleProjectDetail(int finaleProjectId);

    int selectLastInsertedId();

}
