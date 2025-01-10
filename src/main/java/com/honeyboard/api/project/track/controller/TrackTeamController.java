package com.honeyboard.api.project.track.controller;

import com.honeyboard.api.project.track.model.TrackProjectStatus;
import com.honeyboard.api.project.track.model.TrackTeam;
import com.honeyboard.api.project.track.service.TrackTeamService;
import com.honeyboard.api.user.model.UserName;
import com.honeyboard.api.user.service.UserService;
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
public class TrackTeamController {
    private final TrackTeamService trackTeamService;
    private final UserService userService;

    @GetMapping("/{projectId}/status")
    public ResponseEntity<?> getTeamStatus(@PathVariable("projectId") int projectId,
                                           @RequestParam("generation") int generationId) {
        log.info("팀 제출현황 조회 요청 - 프로젝트 ID: {}, 기수: {}", projectId, generationId);
        TrackProjectStatus teamStatus = trackTeamService.getProjectStatus(projectId, generationId);
        if (teamStatus == null) {
            log.info("팀 제출현황 조회 완료, 값 없음 - 프로젝트 ID: {}, 기수: {}", projectId, generationId);
            return ResponseEntity.noContent().build();
        }
        log.info("팀 제출현황 조회 완료 - 프로젝트 ID: {}, 기수: {}", projectId, generationId);
        return ResponseEntity.status(HttpStatus.OK).body(teamStatus);
    }

    @PostMapping("/{projectId}/team")
    public ResponseEntity<?> createTrackTeam(@RequestBody TrackTeam trackTeam, @PathVariable("projectId") int projectId) {
        log.info("관통 팀 생성 요청 - 기수 : {}", trackTeam.getGenerationId());
        trackTeam.setTrackProjectId(projectId);
        trackTeamService.addTrackTeam(trackTeam);
        log.info("관통 팀 생성 완료 - ID: {}", trackTeam.getId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{projectId}/team/update")
    public ResponseEntity<?> updateTrackTeam(@PathVariable int projectId,
                                             @RequestBody TrackTeam trackTeam) {
        log.info("관통 팀 수정 요청 - ID : {}", trackTeam.getMembers().get(0).getTrackTeamId());
        trackTeamService.updateTrackTeam(trackTeam);
        log.info("관통 팀 수정 완료 - ID : {}", trackTeam.getMembers().get(0).getTrackTeamId());
        return ResponseEntity.ok().build();
    }


    @DeleteMapping("/{projectId}/team/{teamId}")
    public ResponseEntity<?> deleteTrackTeam(@PathVariable("teamId") int teamId, @PathVariable("projectId") int projectId) {
        log.info("관통 팀 삭제 요청 - ID : {}", teamId);
        trackTeamService.removeTrackTeam(teamId);
        log.info("관통 팀 삭제 완료 - ID : {}", teamId);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/{projectId}/team/remaining")
    public ResponseEntity<List<UserName>> getRemainedUsers (
            @PathVariable int projectId,
            @RequestParam(required = false) Integer generationId) {

        if (generationId == null) {
            generationId = userService.getActiveGenerationId();
        }

        List<UserName> remainedUsers = trackTeamService.getRemainedUsers(generationId, projectId);
        return remainedUsers.isEmpty() ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(remainedUsers);
    }
}
