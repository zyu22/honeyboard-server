package com.honeyboard.api.project.finale.service;

import com.honeyboard.api.exception.DuplicateTeamMemberException;
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
    @Transactional
    public void createTeam(FinaleTeamRequest request) {
        validateTeamRequest(request);

        try {
            // 중복 멤버 체크
            List<Integer> existingMembers = finaleTeamMapper.findExistingTeamMembers(request.getMemberIds(), null);
            if (!existingMembers.isEmpty()) {
                String memberIds = existingMembers.stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(", "));
                throw new DuplicateTeamMemberException(
                        "이미 팀에 속해있는 멤버가 있습니다. (사용자 ID: " + memberIds + ")"
                );
            }

            // 팀 생성을 위한 데이터 설정
            FinaleTeam newTeam = new FinaleTeam();
            newTeam.setProjectName(request.getProjectName());
            newTeam.setSummary(request.getSummary());
            newTeam.setGenerationId(request.getGenerationId());
            newTeam.setCompleted(false);

            // 팀장을 제외한 팀원 목록 생성
            List<Integer> memberIds = request.getMemberIds().stream()
                    .filter(id -> !id.equals(request.getLeaderId()))
                    .collect(Collectors.toList());

            // 팀과 팀원 정보를 한 번에 저장
            finaleTeamMapper.insertFinaleTeamWithMembers(newTeam, request.getLeaderId(), memberIds);
        } catch (Exception e) {
            log.error("팀 생성 실패: {}", e.getMessage());
            throw new RuntimeException("팀 생성에 실패했습니다.", e);
        }
    }


    @Override
    @Transactional
    public void updateTeam(FinaleTeamRequest request) {
        validateTeamRequest(request);

        if (!finaleTeamMapper.existsTeamById(request.getTeamId())) {
            throw new IllegalArgumentException("존재하지 않는 팀입니다.");
        }

        try {
            // 다른 팀에 속한 멤버 체크
            List<Integer> existingMembers = finaleTeamMapper.findExistingTeamMembers(
                    request.getMemberIds(),
                    request.getTeamId()
            );

            if (!existingMembers.isEmpty()) {
                String memberIds = existingMembers.stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(", "));
                throw new DuplicateTeamMemberException(
                        "다른 팀에 이미 속해있는 멤버가 있습니다. (사용자 ID: " + memberIds + ")"
                );
            }

            // 팀장을 제외한 팀원 목록 생성
            List<Integer> memberIds = request.getMemberIds().stream()
                    .filter(id -> !id.equals(request.getLeaderId()))
                    .collect(Collectors.toList());

            // 팀 정보와 멤버 정보를 한 번에 업데이트
            finaleTeamMapper.updateTeamMembers(
                    request.getTeamId(),
                    request.getLeaderId(),
                    memberIds
            );
        } catch (Exception e) {
            log.error("팀 수정 실패 - 팀 ID: {}, 오류: {}", request.getTeamId(), e.getMessage());
            throw new RuntimeException("팀 수정에 실패했습니다.", e);
        }
    }

    private void validateTeamRequest(FinaleTeamRequest team) {
        if (team.getProjectName() == null || team.getProjectName().trim().isEmpty()) {
            throw new IllegalArgumentException("프로젝트 이름은 필수입니다.");
        }
        if (team.getSummary() == null || team.getSummary().trim().isEmpty()) {
            throw new IllegalArgumentException("프로젝트 설명은 필수입니다.");
        }
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
