package com.honeyboard.api.project.track.controller;

import com.honeyboard.api.common.model.CreateResponse;
import com.honeyboard.api.project.model.ProjectUserInfo;
import com.honeyboard.api.project.track.model.request.TrackProjectRequest;
import com.honeyboard.api.project.track.model.response.TrackProjectDetail;
import com.honeyboard.api.project.track.model.response.TrackProjectList;
import com.honeyboard.api.project.track.service.TrackProjectService;
import com.honeyboard.api.user.model.CurrentUser;
import com.honeyboard.api.user.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/project/track")
@RequiredArgsConstructor
@Slf4j
public class TrackProjectController {
    private final TrackProjectService trackProjectService;

    // 관통프로젝트 전체 조회
    @GetMapping
    public ResponseEntity<?> getAllTrackProjects(
            @RequestParam(required = false, defaultValue = "0") int generationId) {
        log.info("관통 프로젝트 전체 조회 요청 - 기수: {}", generationId);
        List<TrackProjectList> projects = trackProjectService.getAllTrackProjects(generationId);
        log.info("관통 프로젝트 전체 조회 완료 - 기수: {}", generationId);
        return projects.isEmpty() ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(projects);
    }

    // 관통 프로젝트 상세 조회
    @GetMapping("/{trackProjectId}")
    public ResponseEntity<TrackProjectDetail> getTrackProject(@PathVariable int trackProjectId) {
        log.info("관통 프로젝트 상세 조회 요청 - ID: {}", trackProjectId);
        TrackProjectDetail project = trackProjectService.getTrackProjectById(trackProjectId);
        return project == null ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(project);
    }

    // 관통 프로젝트 가능한 인원 검색
    @GetMapping("/{trackProjectId}/available-user")
    public ResponseEntity<?> getTrackProjectMembers(
            @PathVariable("trackProjectId") int trackProjectId) {
        log.info("관통 프로젝트 멤버 조회 요청 - 프로젝트 ID: {}", trackProjectId);
        List<ProjectUserInfo> members = trackProjectService.getTrackProjectMembers(trackProjectId);
        return members.isEmpty() ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(members);
    }

    // 관통프로젝트 생성
    @PostMapping
    public ResponseEntity<CreateResponse> createTrackProject(
            @RequestBody TrackProjectRequest trackProject,
            @CurrentUser User user) {
        // 사용자 ID가 null인 경우 처리
        if (user == null || user.getUserId() == 0) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        log.info("관통 프로젝트 생성 요청 - 유저 ID: {}", user.getUserId());
        CreateResponse createResponse = trackProjectService.createTrackProject(trackProject, user.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(createResponse);
    }

    @PutMapping("/{trackProjectId}")
    public ResponseEntity<?> updateTrackProject(
            @PathVariable("trackProjectId") int trackProjectId,
            @RequestBody TrackProjectRequest trackProject) {
        log.info("관통 프로젝트 수정 요청 - ID: {}", trackProjectId);
        trackProjectService.updateTrackProject(trackProjectId, trackProject);
        log.info("관통 프로젝트 수정 완료 - ID: {}", trackProjectId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{trackProjectId}")
    public ResponseEntity<Void> deleteTrackProject(@PathVariable("trackProjectId") int trackProjectId) {
        log.info("관통 프로젝트 삭제 요청 - ID: {}", trackProjectId);
        trackProjectService.deleteTrackProject(trackProjectId);
        log.info("관통 프로젝트 삭제 완료 - ID: {}", trackProjectId);
        return ResponseEntity.ok().build();
    }
}
