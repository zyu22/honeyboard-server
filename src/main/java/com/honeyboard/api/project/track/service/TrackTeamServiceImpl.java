package com.honeyboard.api.project.track.service;

import com.honeyboard.api.project.track.mapper.TrackTeamMapper;
import com.honeyboard.api.project.track.model.TrackProjectStatus;
import com.honeyboard.api.project.track.model.TrackTeam;
import com.honeyboard.api.project.track.model.TrackTeamMember;
import com.honeyboard.api.user.model.UserName;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrackTeamServiceImpl implements TrackTeamService {
    private final TrackTeamMapper trackTeamMapper;


    @Override
    public TrackProjectStatus getProjectStatus(int projectId, int generationId) {
        log.info("프로젝트 상태 조회 시작 - 기수: {}, 프로젝트: {}", generationId, projectId);
        TrackProjectStatus status = trackTeamMapper.selectTrackProjectStatus(generationId, projectId);
        log.info("프로젝트 상태 조회 완료");
        return status;
    }

    @Override
    @Transactional
    public void addTrackTeam(TrackTeam trackTeam) {
        try {
            log.info("팀 생성 시작 - 기수: {}", trackTeam.getGenerationId());
            trackTeamMapper.insertTrackTeam(trackTeam);
            int teamId = trackTeam.getId();

            if (teamId <= 0) {
                throw new IllegalArgumentException("팀 생성에 실패했습니다.");
            }

            if (trackTeam.getMembers() == null || trackTeam.getMembers().isEmpty()) {
                throw new IllegalArgumentException("팀 멤버가 없습니다.");
            }

            List<TrackTeamMember> teamMembers = trackTeam.getMembers();
            for(TrackTeamMember teamMember : teamMembers) {
                teamMember.setTrackTeamId(teamId);
            }
            trackTeamMapper.insertTrackTeamMember(trackTeam);

            log.info("팀 생성 완료 - 팀 인원 수: {}", trackTeam.getMembers().size());
        } catch (Exception e) {
            log.error("팀 생성 실패 - 기수: {}, 오류: {}", trackTeam.getGenerationId(), e.getMessage());
            throw new RuntimeException("팀 생성 중 오류가 발생했습니다: " + e.getMessage(), e);
        }

    }

    @Override
    public void updateTrackTeam(TrackTeam trackTeam) {
        log.info("팀 변경 시작 - ID: {}", trackTeam.getMembers().get(0).getTrackTeamId());

        List<TrackTeamMember> members = trackTeam.getMembers();
        int updatedRow = trackTeamMapper.updateTrackTeamMembers(members);

        // 3. 업데이트 실패 시 예외 처리
        if (updatedRow == 0) {
            throw new IllegalArgumentException("팀 변경에 실패했습니다.");
        }
        log.info("팀 변경 성공 - ID: {}, 변경된 행 수: {}", trackTeam.getMembers().get(0).getTrackTeamId(), updatedRow);
    }


    @Override
    public void removeTrackTeam(int teamId) {
        log.info("팀 삭제 시작 - ID: {}", teamId);

        // 팀 삭제 (Cascade 처리로 팀 멤버 자동 삭제)
        int deletedRow = trackTeamMapper.deleteTrackTeam(teamId);
        if (deletedRow == 0) {
            throw new IllegalArgumentException("팀 삭제에 실패했습니다.");
        }
        log.info("팀 삭제 완료 - ID: {}", teamId);
    }

    @Override
    public List<UserName> getRemainedUsers(Integer generationId, int projectId) {
        if (generationId <= 0) {
            throw new IllegalArgumentException("유효하지 않은 기수 ID입니다.");
        }
        return trackTeamMapper.selectRemainingUsers(generationId, projectId);
    }
}
