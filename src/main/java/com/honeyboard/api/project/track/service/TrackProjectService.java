package com.honeyboard.api.project.track.service;

import com.honeyboard.api.common.model.CreateResponse;
import com.honeyboard.api.project.model.ProjectUserInfo;
import com.honeyboard.api.project.track.model.request.TrackProjectRequest;
import com.honeyboard.api.project.track.model.response.TrackProjectDetail;
import com.honeyboard.api.project.track.model.response.TrackProjectList;
import com.honeyboard.api.user.model.User;

import java.util.List;

public interface TrackProjectService {
    // 관통 프로젝트 전체 조회
    List<TrackProjectList> getAllTrackProjects(int generationId);
    
    // 관통 프로젝트 상세 조회
    TrackProjectDetail getTrackProjectById(int trackId);
    
    // 프로젝트 멤버 조회
    List<ProjectUserInfo> getTrackProjectMembers(int trackProjectId);
    
    // 프로젝트 생성
    CreateResponse createTrackProject(TrackProjectRequest trackProject, int userId);
    
    // 프로젝트 수정
    void updateTrackProject(int trackProjectId, TrackProjectRequest trackProject);
    
    // 프로젝트 삭제
    void deleteTrackProject(int trackProjectId);
}
