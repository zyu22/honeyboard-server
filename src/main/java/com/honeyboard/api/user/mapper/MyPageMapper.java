package com.honeyboard.api.user.mapper;

import com.honeyboard.api.user.model.mypage.MyAlgorithmSolution;
import com.honeyboard.api.user.model.mypage.MyFinaleProject;
import com.honeyboard.api.user.model.mypage.MyTrackProject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MyPageMapper {

    /* 1) 내가 참여한 트랙 프로젝트 조회 */
    List<MyTrackProject> selectAllMyTrackProjects(@Param("userId") int userId);

    /* 1-1) 해당 트랙 팀에 속한 멤버 목록 조회 */
    List<String> selectTrackTeamMembers(@Param("trackTeamId") int trackTeamId);

    /* 2) 내가 참여한 파이널 프로젝트 조회 */
    List<MyFinaleProject> selectAllMyFinaleProjects(@Param("userId") int userId);

    /* 3) 내가 작성한 알고리즘 풀이 조회 */
    List<MyAlgorithmSolution> selectAllMyAlgorithmSolutions(@Param("userId") int userId);

}
