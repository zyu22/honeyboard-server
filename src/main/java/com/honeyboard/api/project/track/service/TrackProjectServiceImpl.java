package com.honeyboard.api.project.track.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.honeyboard.api.common.model.CreateResponse;
import com.honeyboard.api.project.model.ProjectUserInfo;
import com.honeyboard.api.project.track.mapper.TrackProjectMapper;
import com.honeyboard.api.project.track.model.request.TrackProjectRequest;
import com.honeyboard.api.project.track.model.response.TrackProjectDetail;
import com.honeyboard.api.project.track.model.response.TrackProjectList;
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

    // 관통 프로젝트 전체 조회
    @Override
    public List<TrackProjectList> getAllTrackProjects(int generationId) {
        log.info("전체 관통 프로젝트 조회 시작 - 기수: {}", generationId);
        return trackProjectMapper.selectAllTrackProjects(generationId);
    }

    
    // 관통프로젝트 상세조회
    @Override
    public TrackProjectDetail getTrackProjectById(int trackId) {
        if (trackId <= 0) {
            throw new IllegalArgumentException("유효하지 않은 프로젝트 ID입니다.");
        }
        log.info("관통 프로젝트 상세 조회 시작 - ID: {}", trackId);
        return trackProjectMapper.selectTrackProjectById(trackId);
    }

    // 관통 프로젝트 가능한 멤버 조회
    @Override
    public List<ProjectUserInfo> getTrackProjectMembers(int trackProjectId) {
        log.info("관통 프로젝트 멤버 조회 시작 - 프로젝트 ID: {}", trackProjectId);
        return trackProjectMapper.selectTrackProjectMembers(trackProjectId);
    }

    // 관통프로젝트 생성
    @Override
    @Transactional
    public CreateResponse createTrackProject(TrackProjectRequest trackProject, int userId) {
        validateTrackProject(trackProject);

        log.info("관통 프로젝트 생성 시작 - 설명: {}", trackProject.getDescription());

        CreateResponse createResponse = new CreateResponse();
        int insertResult = trackProjectMapper.insertTrackProject(trackProject, userId, createResponse);
        if (insertResult == 0) {
            log.info("관통 프로젝트 생성 실패");
            throw new RuntimeException("프로젝트 생성에 실패했습니다.");
        }

        int trackId = createResponse.getId();
        // 제외 인원 추가
        List<Integer> excludedMemberIds = trackProject.getExcludedMembers();
        if (excludedMemberIds != null && !excludedMemberIds.isEmpty()) {
            validateExcludedMembers(excludedMemberIds);
            trackProjectMapper.insertExcludedMembers(trackId, excludedMemberIds);
        }

        log.info("관통 프로젝트 생성 완료 - ID: {}", trackId);
        return new CreateResponse(trackId);
    }

    @Override
    @Transactional
    public void updateTrackProject(int trackProjectId, TrackProjectRequest trackProject) {
        if (trackProjectId <= 0) {
            throw new IllegalArgumentException("유효하지 않은 프로젝트 ID입니다.");
        }
        
        // 유효성 검사
        validateTrackProject(trackProject);

        log.info("관통 프로젝트 수정 시작 - ID: {}", trackProjectId);

        int result = trackProjectMapper.updateTrackProject(trackProjectId, trackProject);
        if (result == 0) {
            throw new RuntimeException("프로젝트 수정에 실패했습니다.");
        }

        // 1. 현재 DB에 저장된 제외 인원 목록 조회
        final List<Integer> currentExcluded = trackProjectMapper.selectExcludedMembers(trackProjectId);

        // 2. 새로 요청된 제외 인원 목록 (null 처리)
        final List<Integer> newExcluded = Optional.ofNullable(trackProject.getExcludedMembers())
                .orElse(new ArrayList<>());

        // 3. 제거할 멤버 계산: 현재 있는데 새 목록에는 없는 멤버
        List<Integer> toRemove = currentExcluded.stream()
                .filter(id -> !newExcluded.contains(id))
                .collect(Collectors.toList());

        // 4. 추가할 멤버 계산: 새 목록에는 있는데 현재 없는 멤버
        List<Integer> toAdd = newExcluded.stream()
                .filter(id -> !currentExcluded.contains(id))
                .collect(Collectors.toList());

        // 5. 변경사항 적용
        if (!toRemove.isEmpty()) {
            trackProjectMapper.deleteSelectedExcludedMembers(trackProjectId, toRemove);
        }
        if (!toAdd.isEmpty()) {
            trackProjectMapper.insertExcludedMembers(trackProjectId, toAdd);
        }


        log.info("관통 프로젝트 수정 성공 - ID: {}", trackProjectId);
    }

    @Override
    @Transactional
    public void deleteTrackProject(int trackProjectId) {
        if (trackProjectId <= 0) {
            throw new IllegalArgumentException("유효하지 않은 프로젝트 ID입니다.");
        }

        log.info("관통 프로젝트 삭제 시작 - ID: {}", trackProjectId);

        if (!trackProjectMapper.existsById(trackProjectId)) {
            throw new IllegalArgumentException("존재하지 않는 프로젝트입니다.");
        }

        int result = trackProjectMapper.deleteTrackProject(trackProjectId);
        if (result != 1) {
            throw new RuntimeException("프로젝트 삭제에 실패했습니다.");
        }

        log.info("관통 프로젝트 삭제 완료 - ID: {}", trackProjectId);
    }

    private void validateTrackProject(TrackProjectRequest trackProject) {
        if (trackProject == null) {
            throw new IllegalArgumentException("프로젝트 정보가 없습니다.");
        }
        if (trackProject.getObjective() == null || trackProject.getObjective().trim().isEmpty()) {
            throw new IllegalArgumentException("프로젝트 목표를 입력해주세요.");
        }
    }

    private void validateExcludedMembers(List<Integer> memberIds) {
        if (memberIds.stream().anyMatch(id -> id <= 0)) {
            throw new IllegalArgumentException("유효하지 않은 사용자 ID가 포함되어 있습니다.");
        }
    }
}
