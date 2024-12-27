package com.honeyboard.api.project.track.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.honeyboard.api.project.track.model.TrackProject;
import com.honeyboard.api.user.model.User;

@Mapper
public interface TrackProjectMapper {
    // 전체 조회
    List<TrackProject> selectAllTrackProjects(@Param("generationId") Integer generationId);
    
    // 상세 조회
    TrackProject selectTrackProjectById(@Param("trackId") int trackId);
    
    // 프로젝트 멤버 조회
    List<User> selectTrackProjectMembers(@Param("generationId") Integer generationId);
    
    // 프로젝트 생성
    int insertTrackProject(TrackProject trackProject);
    
    // 제외 멤버 등록
    int insertExcludedMembers(@Param("trackProjectId") int trackProjectId, 
                            @Param("userIds") List<Integer> userIds);
    
    // 프로젝트 수정
    int updateTrackProject(@Param("trackId") int trackId, 
                          @Param("trackProject") TrackProject trackProject);
    
    // 프로젝트 삭제
    int deleteTrackProject(@Param("trackId") int trackId);
    
    // 존재 여부 확인
    boolean existsById(@Param("trackId") int trackId);
    
 	// 제외 멤버 조회
    List<Integer> selectExcludedMembers(@Param("projectId") int projectId);
}
