package com.honeyboard.api.project.finale.service;

import com.honeyboard.api.exception.BusinessException;
import com.honeyboard.api.exception.ErrorCode;
import com.honeyboard.api.project.finale.mapper.FinaleTeamMapper;
import com.honeyboard.api.user.model.UserName;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
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

        return finaleTeamMapper.selectSubmitStatusByDate(targetDate);
    }

    @Override
    public List<UserName> getRemainedUsers(int generationId) {
        if (generationId <= 0) {
            throw new IllegalArgumentException("유효하지 않은 기수 ID입니다.");
        }

        return finaleTeamMapper.selectRemainedUsers(generationId);
    }

    @Override
    @Transactional
    public void createTeam(FinaleTeamRequest request) {
        validateTeamRequest(request);

        // 중복 멤버 체크
        List<Integer> existingMembers = finaleTeamMapper.findExistingTeamMembers(request.getMemberIds(), null);
        if (!existingMembers.isEmpty()) {
            String memberIds = existingMembers.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(", "));
            throw new BusinessException(
                    ErrorCode.DUPLICATE_TEAM_MEMBER,
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
    }


    @Override
    @Transactional
    public void updateTeam(FinaleTeamRequest request) {
        validateTeamRequest(request);

        if (!finaleTeamMapper.existsTeamById(request.getTeamId())) {
            throw new IllegalArgumentException("존재하지 않는 팀입니다.");
        }

        List<Integer> existingMembers = finaleTeamMapper.findExistingTeamMembers(
                request.getMemberIds(),
                request.getTeamId()
        );

        if (!existingMembers.isEmpty()) {
            String memberIds = existingMembers.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(", "));
            throw new BusinessException(
                    ErrorCode.DUPLICATE_TEAM_MEMBER,
                    "이미 팀에 속해있는 멤버가 있습니다. (사용자 ID: " + memberIds + ")"
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
