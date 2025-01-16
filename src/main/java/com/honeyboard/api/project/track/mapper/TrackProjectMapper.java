package com.honeyboard.api.project.track.mapper;

import com.honeyboard.api.project.model.ProjectUserInfo;
import com.honeyboard.api.project.track.model.request.TrackProjectRequest;
import com.honeyboard.api.project.track.model.response.TrackProjectDetail;
import com.honeyboard.api.project.track.model.response.TrackProjectList;
import com.honeyboard.api.user.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TrackProjectMapper {
    // 관통 프로젝트 전체 조회
    List<TrackProjectList> selectAllTrackProjects(@Param("generationId") int generationId);

    // 관통 프로젝트 상세 조회
    TrackProjectDetail selectTrackProjectById(@Param("trackProjectId") int trackProjectId);

    // 프로젝트 멤버 조회
    List<ProjectUserInfo> selectTrackProjectMembers(@Param("trackProjectId") int trackProjectId);

    // 관통 프로젝트 생성
    int insertTrackProject(@Param("trackProject") TrackProjectRequest trackProject, @Param("userId") int userId);

    // 제외 멤버 등록
    int insertExcludedMembers(@Param("trackProjectId") int trackProjectId,
                              @Param("excludedMemberIds") List<Integer> excludedMemberIds);

    // 제외 멤버 삭제
    int deleteSelectedExcludedMembers(@Param("trackProjectId") int trackProjectId,
                                      @Param("excludedMemberId") List<Integer> memberIds);

    // 제외 인원 조회
    List<Integer> selectExcludedMembers(@Param("trackProjectId") int trackProjectId);

    // 프로젝트 수정
    int updateTrackProject(@Param("trackProjectId") int trackProjectId,
                           @Param("trackProject") TrackProjectRequest trackProject);

    // 프로젝트 삭제
    int deleteTrackProject(@Param("trackProjectId") int trackProjectId);

    // 존재 여부 확인
    boolean existsById(@Param("trackProjectId") int trackProjectId);

}
