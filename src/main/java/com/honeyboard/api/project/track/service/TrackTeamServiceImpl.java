package com.honeyboard.api.project.track.service;

import com.honeyboard.api.project.model.TeamRequest;
import com.honeyboard.api.project.track.mapper.TrackTeamMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrackTeamServiceImpl implements TrackTeamService {
    private final TrackTeamMapper trackTeamMapper;

    @Override
    @Transactional
    public void addTrackTeam(int trackProjectId, TeamRequest trackTeam) {
        // 입력값 검증
        if (trackProjectId <= 0) {
            throw new IllegalArgumentException("유효하지 않은 프로젝트 ID 입니다.");
        }
        if (trackTeam == null || trackTeam.getLeaderId() <= 0) {
            throw new IllegalArgumentException("팀장 정보가 유효하지 않습니다.");
        }

        // 팀장이나 팀원이 이미 다른 팀에 속해있는지 체크
        if (trackTeamMapper.existsByProjectIdAndUserId(trackProjectId, trackTeam.getLeaderId())) {
            throw new IllegalStateException("팀장이 이미 다른 팀에 속해있습니다.");
        }

        log.info("팀 생성 시작 - 프로젝트 ID: {}", trackProjectId);
        int insertResult = trackTeamMapper.insertTrackTeam(trackProjectId);

        if(insertResult == 0) {
            log.info("팀 생성 실패 - 프로젝트 ID: {}", trackProjectId);
            throw new RuntimeException("팀 생성에 실패하였습니다.");
        }
        log.info("팀 생성 성공 및 팀 ID 조회");
        int teamId = trackTeamMapper.getLastInsertedTeamId();

        log.info("팀장 추가 시작 - 팀장 ID: {}", trackTeam.getLeaderId());
        int result = trackTeamMapper.insertTeamLeader(teamId, trackTeam.getLeaderId());
        if(result == 0) {
            log.info("팀장 추가 실패 - 팀장 ID: {}", trackTeam.getLeaderId());
            throw new RuntimeException("팀장 추가에 실패하였습니다.");
        }
        log.info("팀장 추가 성공 - 팀장 ID: {}", trackTeam.getLeaderId());

        log.info("팀원 추가 시작");
        if (trackTeam.getMemberIds() != null && !trackTeam.getMemberIds().isEmpty()) {
            int membersResult = trackTeamMapper.insertTeamMembers(teamId, trackTeam.getMemberIds());
            if (membersResult != trackTeam.getMemberIds().size()) {
                log.error("팀원 추가 실패 - 예상: {}, 실제: {}", trackTeam.getMemberIds().size(), membersResult);
                throw new RuntimeException("일부 팀원 추가에 실패하였습니다.");
            }
        }
        log.info("팀원 추가 성공");
    }

    @Override
    @Transactional
    public void updateTrackTeam(int trackProjectId, int trackTeamId, TeamRequest trackTeam) {
        // 1. 기존 팀 정보 조회 (팀장 ID 조회)
        int currentLeaderId = trackTeamMapper.getTeamLeaderId(trackTeamId);

        // 2. 팀장이 변경되었는지 확인
        if (currentLeaderId != trackTeam.getLeaderId()) {
            // 새 팀장이 이미 다른 팀에 속해있는지 확인
            if (trackTeamMapper.existsByProjectIdAndUserId(trackTeamId, trackTeam.getLeaderId())) {
                throw new IllegalStateException("새 팀장이 이미 다른 팀에 속해있습니다.");
            }
            // 팀장 변경
            trackTeamMapper.updateTeamLeader(trackTeamId, currentLeaderId, trackTeam.getLeaderId());
        }

        // 3. 기존 팀원 전체 삭제 (팀장 제외)
        trackTeamMapper.deleteAllTeamMembers(trackTeamId);

        // 4. 새 팀원 추가
        if (trackTeam.getMemberIds() != null && !trackTeam.getMemberIds().isEmpty()) {
            // 새 팀원들이 이미 다른 팀에 속해있는지 확인
            for (Integer memberId : trackTeam.getMemberIds()) {
                if (trackTeamMapper.existsByProjectIdAndUserId(trackTeamId, memberId)) {
                    throw new IllegalStateException("팀원(ID: " + memberId + ")이 이미 다른 팀에 속해있습니다.");
                }
            }
            // 새 팀원 추가
            trackTeamMapper.insertTeamMembers(trackTeamId, trackTeam.getMemberIds());
        }
    }
}
