package com.honeyboard.api.project.track.service;

import java.util.List;

import com.honeyboard.api.project.track.mapper.TrackProjectMapper;
import com.honeyboard.api.project.track.model.TrackProject;
import com.honeyboard.api.user.model.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrackProjectServiceImpl implements TrackProjectService {
    
    private final TrackProjectMapper trackProjectMapper;

    @Override
    public List<TrackProject> getAllTrackProjects(Integer generationId) {
        log.info("전체 관통 프로젝트 조회 시작 - 기수: {}", generationId);
        List<TrackProject> projects = trackProjectMapper.selectAllTrackProjects(generationId);
        log.info("전체 관통 프로젝트 조회 완료 - 조회된 프로젝트 수: {}", projects.size());
        return projects;
    }

    @Override
    public TrackProject getTrackProjectById(int trackId) {
        log.info("관통 프로젝트 상세 조회 시작 - ID: {}", trackId);
        TrackProject project = trackProjectMapper.selectTrackProjectById(trackId);
        if (project == null) {
            log.error("프로젝트를 찾을 수 없음 - ID: {}", trackId);
            throw new RuntimeException("프로젝트를 찾을 수 없습니다.");
        }
        return project;
    }

    @Override
    public List<User> getTrackProjectMembers(Integer generationId) {
        log.info("관통 프로젝트 멤버 조회 시작 - 기수: {}", generationId);
        List<User> members = trackProjectMapper.selectTrackProjectMembers(generationId);
        log.info("관통 프로젝트 멤버 조회 완료 - 조회된 멤버 수: {}", members.size());
        return members;
    }

    @Override
    @Transactional
    public TrackProject createTrackProject(TrackProject trackProject, List<Integer> excludedMemberIds) {
        log.info("관통 프로젝트 생성 시작 - 제목: {}", trackProject.getTitle());
        
        int result = trackProjectMapper.insertTrackProject(trackProject);
        if (result != 1) {
            log.error("프로젝트 생성 실패 - 제목: {}", trackProject.getTitle());
            throw new RuntimeException("프로젝트 생성에 실패했습니다.");
        }

        if (excludedMemberIds != null && !excludedMemberIds.isEmpty()) {
            trackProjectMapper.insertExcludedMembers(trackProject.getId(), excludedMemberIds);
        }

        log.info("관통 프로젝트 생성 완료 - ID: {}", trackProject.getId());
        return trackProject;
    }

    @Override
    @Transactional
    public TrackProject updateTrackProject(int trackId, TrackProject trackProject) {
        log.info("관통 프로젝트 수정 시작 - ID: {}", trackId);
        
        if (!trackProjectMapper.existsById(trackId)) {
            log.error("수정할 프로젝트를 찾을 수 없음 - ID: {}", trackId);
            throw new RuntimeException("프로젝트를 찾을 수 없습니다.");
        }

        int result = trackProjectMapper.updateTrackProject(trackId, trackProject);
        if (result != 1) {
            log.error("프로젝트 수정 실패 - ID: {}", trackId);
            throw new RuntimeException("프로젝트 수정에 실패했습니다.");
        }

        log.info("관통 프로젝트 수정 완료 - ID: {}", trackId);
        return trackProject;
    }

    @Override
    @Transactional
    public boolean deleteTrackProject(int trackId) {
        log.info("관통 프로젝트 삭제 시작 - ID: {}", trackId);
        
        if (!trackProjectMapper.existsById(trackId)) {
            log.error("삭제할 프로젝트를 찾을 수 없음 - ID: {}", trackId);
            throw new RuntimeException("프로젝트를 찾을 수 없습니다.");
        }

        int result = trackProjectMapper.deleteTrackProject(trackId);
        log.info("관통 프로젝트 삭제 " + (result == 1 ? "완료" : "실패") + " - ID: {}", trackId);
        return result == 1;
    }
}
