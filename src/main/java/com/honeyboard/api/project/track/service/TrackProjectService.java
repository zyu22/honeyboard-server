package com.honeyboard.api.project.track.service;

import com.honeyboard.api.project.track.model.TrackProject;
import com.honeyboard.api.user.model.User;

import java.util.List;

public interface TrackProjectService {
    // 전체 조회
    List<TrackProject> getAllTrackProjects(Integer generationId);
    
    // 상세 조회
    TrackProject getTrackProjectById(int trackId);
    
    // 프로젝트 멤버 조회
    List<User> getTrackProjectMembers(Integer generationId);
    
    // 프로젝트 생성
    TrackProject createTrackProject(TrackProject trackProject, List<Integer> excludedMemberIds);
    
    // 프로젝트 수정
    TrackProject updateTrackProject(int trackId, TrackProject trackProject);
    
    // 프로젝트 삭제
    boolean deleteTrackProject(int trackId);
}
