package com.honeyboard.api.project.track.controller;

import com.honeyboard.api.project.track.model.TrackProject;
import com.honeyboard.api.project.track.service.TrackProjectService;
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

    @GetMapping
    public ResponseEntity<?> getAllTrackProjects(
            @RequestParam(required = false) Integer generation
    ) {
        try {
            log.info("관통 프로젝트 전체 조회 요청 - 기수: {}", generation);
            List<TrackProject> projects = trackProjectService.getAllTrackProjects(generation);
            
            if (projects.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
            return ResponseEntity.ok(projects);
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            log.error("관통 프로젝트 조회 중 에러 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{trackId}")
    public ResponseEntity<?> getTrackProject(@PathVariable int trackId) {
        try {
            log.info("관통 프로젝트 상세 조회 요청 - ID: {}", trackId);
            TrackProject project = trackProjectService.getTrackProjectById(trackId);
            return ResponseEntity.ok(project);
        } catch (Exception e) {
            log.error("관통 프로젝트 상세 조회 중 에러 발생 - ID: {}", trackId, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/member")
    public ResponseEntity<?> getTrackProjectMembers(
            @RequestParam(required = false) Integer generation
    ) {
        try {
            log.info("관통 프로젝트 멤버 조회 요청 - 기수: {}", generation);
            List<User> members = trackProjectService.getTrackProjectMembers(generation);
            
            if (members.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
            return ResponseEntity.ok(members);
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception e) {
            log.error("관통 프로젝트 멤버 조회 중 에러 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<?> createTrackProject(
            @RequestBody TrackProject trackProject
    ) {
        try {
            log.info("관통 프로젝트 생성 요청 - 제목: {}", trackProject.getTitle());
            TrackProject createdProject = trackProjectService.createTrackProject(
                trackProject, 
                trackProject.getExcludedMemberIds()
            );
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception e) {
            log.error("관통 프로젝트 생성 중 에러 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{trackId}")
    public ResponseEntity<?> updateTrackProject(
            @PathVariable int trackId,
            @RequestBody TrackProject trackProject
    ) {
        try {
            log.info("관통 프로젝트 수정 요청 - ID: {}", trackId);
            trackProjectService.updateTrackProject(trackId, trackProject);
            return ResponseEntity.ok().build();
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception e) {
            log.error("관통 프로젝트 수정 중 에러 발생 - ID: {}", trackId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{trackId}")
    public ResponseEntity<?> deleteTrackProject(@PathVariable int trackId) {
        try {
            log.info("관통 프로젝트 삭제 요청 - ID: {}", trackId);
            trackProjectService.deleteTrackProject(trackId);
            return ResponseEntity.ok().build();
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            log.error("관통 프로젝트 삭제 중 에러 발생 - ID: {}", trackId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
