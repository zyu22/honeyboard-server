package com.honeyboard.api.project.track.controller;

import com.honeyboard.api.common.model.CreateResponse;
import com.honeyboard.api.project.model.ProjectUserInfo;
import com.honeyboard.api.project.model.TeamRequest;
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

    @PostMapping("/{trackProjectId}/team")
    public ResponseEntity<?> createTrackTeam(@RequestBody TeamRequest trackTeam, @PathVariable("trackProjectId") int trackProjectId) {
        log.info("관통 팀 생성 요청 - 프로젝트 ID : {}", trackProjectId);

        trackTeamService.addTrackTeam(trackProjectId, trackTeam);
        log.info("관통 팀 생성 완료 - Leader ID: {}", trackTeam.getLeaderId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{trackProjectId}/team/{trackTeamId}")
    public ResponseEntity<?> updateTrackTeam(@PathVariable int trackProjectId,
                                             @PathVariable int trackTeamId,
                                             @RequestBody TeamRequest trackTeam) {
        log.info("관통 팀 수정 요청 - 팀 ID : {}", trackTeamId);
        trackTeamService.updateTrackTeam(trackProjectId, trackTeamId, trackTeam);
        log.info("관통 팀 수정 완료 - 팀 ID : {}", trackTeamId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("{trackProjectId}/available-members")
    public ResponseEntity<?> getAvailableMembers(@PathVariable int trackProjectId) {
        log.info("관통 프로젝트 멤버 조회 요청 - 프로젝트 ID: {}", trackProjectId);
        List<ProjectUserInfo> members = trackTeamService.getTrackProjectMembers(trackProjectId);
        return members.isEmpty() ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(members);
    }
}
