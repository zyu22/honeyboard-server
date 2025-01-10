package com.honeyboard.api.project.track.controller;

import com.honeyboard.api.project.track.model.TrackProject;
import com.honeyboard.api.project.track.service.TrackProjectService;
import com.honeyboard.api.user.model.User;
import com.honeyboard.api.user.model.UserInfo;
import com.honeyboard.api.user.model.UserName;
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

    @GetMapping
    public ResponseEntity<List<TrackProject>> getAllTrackProjects(
            @RequestParam(required = false) Integer generation) {
        log.debug("관통 프로젝트 전체 조회 요청 - 기수: {}", generation);
        List<TrackProject> projects = trackProjectService.getAllTrackProjects(generation);
        return projects.isEmpty() ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(projects);
    }

    @GetMapping("/{trackId}")
    public ResponseEntity<TrackProject> getTrackProject(@PathVariable int trackId) {
        log.debug("관통 프로젝트 상세 조회 요청 - ID: {}", trackId);
        TrackProject project = trackProjectService.getTrackProjectById(trackId);
        return project == null ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(project);
    }

    @GetMapping("/member")
    public ResponseEntity<List<User>> getTrackProjectMembers(
            @RequestParam(required = false) Integer generation) {
        log.debug("관통 프로젝트 멤버 조회 요청 - 기수: {}", generation);
        List<User> members = trackProjectService.getTrackProjectMembers(generation);
        return members.isEmpty() ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(members);
    }

    @PostMapping
    public ResponseEntity<TrackProject> createTrackProject(
            @RequestBody TrackProject trackProject) {
        log.debug("관통 프로젝트 생성 요청 - 제목: {}", trackProject.getTitle());
        TrackProject createdProject = trackProjectService.createTrackProject(
                trackProject,
                trackProject.getExcludedMemberIds()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProject);
    }

    @PutMapping("/{trackId}")
    public ResponseEntity<TrackProject> updateTrackProject(
            @PathVariable int trackId,
            @RequestBody TrackProject trackProject) {
        log.debug("관통 프로젝트 수정 요청 - ID: {}", trackId);
        TrackProject updatedProject = trackProjectService.updateTrackProject(trackId, trackProject);
        return ResponseEntity.ok(updatedProject);
    }

    @DeleteMapping("/{trackId}")
    public ResponseEntity<Void> deleteTrackProject(@PathVariable int trackId) {
        log.debug("관통 프로젝트 삭제 요청 - ID: {}", trackId);
        trackProjectService.deleteTrackProject(trackId);
        return ResponseEntity.ok().build();
    }



}
