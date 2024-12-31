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
        if (generationId == null || generationId <= 0) {
            throw new IllegalArgumentException("유효하지 않은 기수 ID입니다.");
        }
        log.debug("전체 관통 프로젝트 조회 시작 - 기수: {}", generationId);
        return trackProjectMapper.selectAllTrackProjects(generationId);
    }

    @Override
    public TrackProject getTrackProjectById(int trackId) {
        if (trackId <= 0) {
            throw new IllegalArgumentException("유효하지 않은 프로젝트 ID입니다.");
        }
        log.debug("관통 프로젝트 상세 조회 시작 - ID: {}", trackId);
        return trackProjectMapper.selectTrackProjectById(trackId);
    }

    @Override
    public List<User> getTrackProjectMembers(Integer generationId) {
        if (generationId == null || generationId <= 0) {
            throw new IllegalArgumentException("유효하지 않은 기수 ID입니다.");
        }
        log.debug("관통 프로젝트 멤버 조회 시작 - 기수: {}", generationId);
        return trackProjectMapper.selectTrackProjectMembers(generationId);
    }

    @Override
    @Transactional
    public TrackProject createTrackProject(TrackProject trackProject, List<Integer> excludedMemberIds) {
        validateTrackProject(trackProject);

        try {
            log.debug("관통 프로젝트 생성 시작 - 제목: {}", trackProject.getTitle());
            int result = trackProjectMapper.insertTrackProject(trackProject);

            if (result != 1) {
                throw new RuntimeException("프로젝트 생성에 실패했습니다.");
            }

            if (excludedMemberIds != null && !excludedMemberIds.isEmpty()) {
                validateExcludedMembers(excludedMemberIds);
                trackProjectMapper.insertExcludedMembers(trackProject.getId(), excludedMemberIds);
            }

            log.info("관통 프로젝트 생성 완료 - ID: {}", trackProject.getId());
            return trackProject;

        } catch (Exception e) {
            log.error("프로젝트 생성 실패 - 제목: {}, 오류: {}", trackProject.getTitle(), e.getMessage());
            throw new RuntimeException("프로젝트 생성 중 오류가 발생했습니다.", e);
        }
    }

    @Override
    @Transactional
    public TrackProject updateTrackProject(int trackId, TrackProject trackProject) {
        if (trackId <= 0) {
            throw new IllegalArgumentException("유효하지 않은 프로젝트 ID입니다.");
        }
        validateTrackProject(trackProject);

        try {
            log.debug("관통 프로젝트 수정 시작 - ID: {}", trackId);

            if (!trackProjectMapper.existsById(trackId)) {
                throw new IllegalArgumentException("존재하지 않는 프로젝트입니다.");
            }

            int result = trackProjectMapper.updateTrackProject(trackId, trackProject);
            if (result != 1) {
                throw new RuntimeException("프로젝트 수정에 실패했습니다.");
            }

            log.info("관통 프로젝트 수정 완료 - ID: {}", trackId);
            return trackProject;

        } catch (Exception e) {
            log.error("프로젝트 수정 실패 - ID: {}, 오류: {}", trackId, e.getMessage());
            throw new RuntimeException("프로젝트 수정 중 오류가 발생했습니다.", e);
        }
    }

    @Override
    @Transactional
    public boolean deleteTrackProject(int trackId) {
        if (trackId <= 0) {
            throw new IllegalArgumentException("유효하지 않은 프로젝트 ID입니다.");
        }

        try {
            log.debug("관통 프로젝트 삭제 시작 - ID: {}", trackId);

            if (!trackProjectMapper.existsById(trackId)) {
                throw new IllegalArgumentException("존재하지 않는 프로젝트입니다.");
            }

            int result = trackProjectMapper.deleteTrackProject(trackId);
            if (result != 1) {
                throw new RuntimeException("프로젝트 삭제에 실패했습니다.");
            }

            log.info("관통 프로젝트 삭제 완료 - ID: {}", trackId);
            return true;

        } catch (Exception e) {
            log.error("프로젝트 삭제 실패 - ID: {}, 오류: {}", trackId, e.getMessage());
            throw new RuntimeException("프로젝트 삭제 중 오류가 발생했습니다.", e);
        }
    }

    private void validateTrackProject(TrackProject trackProject) {
        if (trackProject == null) {
            throw new IllegalArgumentException("프로젝트 정보가 없습니다.");
        }
        if (trackProject.getTitle() == null || trackProject.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("프로젝트 제목을 입력해주세요.");
        }
        if (trackProject.getGenerationId() <= 0) {
            throw new IllegalArgumentException("유효하지 않은 기수 정보입니다.");
        }
    }

    private void validateExcludedMembers(List<Integer> memberIds) {
        if (memberIds.stream().anyMatch(id -> id <= 0)) {
            throw new IllegalArgumentException("유효하지 않은 사용자 ID가 포함되어 있습니다.");
        }
    }

}
