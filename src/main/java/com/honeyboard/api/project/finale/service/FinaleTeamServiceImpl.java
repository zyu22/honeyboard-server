package com.honeyboard.api.project.finale.service;

import com.honeyboard.api.project.finale.mapper.FinaleTeamMapper;
import com.honeyboard.api.project.finale.model.FinaleMember;
import com.honeyboard.api.project.finale.model.FinaleTeamRequest;
import com.honeyboard.api.project.finale.model.FinaleTeam;
import com.honeyboard.api.user.model.UserName;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public
class FinaleTeamServiceImpl implements FinaleTeamService {

    private final FinaleTeamMapper finaleTeamMapper;

    @Override
    public List<FinaleTeam> findStatusByDate(LocalDate targetDate) {
        if (targetDate == null) {
            throw new IllegalArgumentException("조회할 날짜를 입력해주세요.");
        }
        if (targetDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("미래 날짜는 조회할 수 없습니다.");
        }

        try {
            return finaleTeamMapper.selectSubmitStatusByDate(targetDate);
        } catch (Exception e) {
            log.error("팀 상태 조회 실패 - 날짜: {}, 오류: {}", targetDate, e.getMessage());
            throw new RuntimeException("팀 상태 조회에 실패했습니다.", e);
        }
    }

    @Override
    public List<UserName> getRemainedUsers(int generationId) {
        if (generationId <= 0) {
            throw new IllegalArgumentException("유효하지 않은 기수 ID입니다.");
        }

        try {
            return finaleTeamMapper.selectRemainedUsers(generationId);
        } catch (Exception e) {
            log.error("미배정 사용자 조회 실패 - 기수: {}, 오류: {}", generationId, e.getMessage());
            throw new RuntimeException("미배정 사용자 조회에 실패했습니다.", e);
        }
    }

    @Override
    public FinaleTeam createTeam(FinaleTeamRequest request) {
        validateTeamRequest(request);

        FinaleTeam newTeam = new FinaleTeam();
        newTeam.setGenerationId(request.getGenerationId());

        try {
            // 팀 생성
            finaleTeamMapper.insertFinaleTeam(newTeam);

            // 팀장 등록
            finaleTeamMapper.insertTeamMember(
                    newTeam.getTeamId(),
                    request.getLeaderId(),
                    "leader"
            );

            // 팀원들 등록
            for (Integer memberId : request.getMemberIds()) {
                if (!memberId.equals(request.getLeaderId())) {
                    finaleTeamMapper.insertTeamMember(
                            newTeam.getTeamId(),
                            memberId,
                            "member"
                    );
                }
            }

            // 생성된 팀 정보 조회
            FinaleTeam team = finaleTeamMapper.selectTeamById(newTeam.getTeamId());
            team.setMembers(finaleTeamMapper.selectTeamMembers(newTeam.getTeamId()));
            return team;
        } catch (Exception e) {
            log.error("팀 생성 실패: {}", e.getMessage());
            throw new RuntimeException("팀 생성에 실패했습니다.", e);
        }
    }

    @Override
    @Transactional
    public FinaleTeam updateTeam(FinaleTeamRequest request) {
        validateTeamRequest(request);

        if (!finaleTeamMapper.existsTeamById(request.getTeamId())) {
            throw new IllegalArgumentException("존재하지 않는 팀입니다.");
        }

        try {
            // 현재 팀 멤버 조회
            List<FinaleMember> currentMembers = finaleTeamMapper.selectTeamMembers(request.getTeamId());

            // 제거될 멤버 처리
            for (FinaleMember member : currentMembers) {
                if (!request.getMemberIds().contains(member.getUserId())) {
                    finaleTeamMapper.deleteTeamMember(request.getTeamId(), member.getUserId());
                }
            }

            // 현재 멤버 ID 목록 생성
            Set<Integer> currentMemberIds = currentMembers.stream()
                    .map(FinaleMember::getUserId)
                    .collect(Collectors.toSet());

            // 새로운 멤버 추가 및 역할 설정
            for (Integer memberId : request.getMemberIds()) {
                if (!currentMemberIds.contains(memberId)) {
                    String role = memberId.equals(request.getLeaderId()) ? "leader" : "member";
                    finaleTeamMapper.insertTeamMember(request.getTeamId(), memberId, role);
                } else if (memberId.equals(request.getLeaderId())) {
                    finaleTeamMapper.deleteTeamMember(request.getTeamId(), memberId);
                    finaleTeamMapper.insertTeamMember(request.getTeamId(), memberId, "leader");
                }
            }

            // 수정된 팀 정보 조회
            FinaleTeam team = finaleTeamMapper.selectTeamById(request.getTeamId());
            team.setMembers(finaleTeamMapper.selectTeamMembers(request.getTeamId()));
            return team;
        } catch (Exception e) {
            log.error("팀 수정 실패 - 팀 ID: {}, 오류: {}", request.getTeamId(), e.getMessage());
            throw new RuntimeException("팀 수정에 실패했습니다.", e);
        }
    }



    private void validateTeamRequest(FinaleTeamRequest team) {
        if (team.getGenerationId() == null || team.getGenerationId() <= 0) {
            throw new IllegalArgumentException("유효하지 않은 기수 ID입니다.");
        }
        if (team.getLeaderId() == null || team.getLeaderId() <= 0) {
            throw new IllegalArgumentException("팀장 정보가 필요합니다.");
        }
        if (team.getMemberIds() == null || team.getMemberIds().isEmpty()) {
            throw new IllegalArgumentException("최소 1명 이상의 팀원이 필요합니다.");
        }
    }

}
