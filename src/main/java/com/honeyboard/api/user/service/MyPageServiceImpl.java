package com.honeyboard.api.user.service;

import com.honeyboard.api.user.mapper.MyPageMapper;
import com.honeyboard.api.user.model.mypage.MyAlgorithmSolution;
import com.honeyboard.api.user.model.mypage.MyFinaleProject;
import com.honeyboard.api.user.model.mypage.MyTrackProject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MyPageServiceImpl implements MyPageService {

    private final MyPageMapper myPageMapper;

    @Override
    public List<MyTrackProject> getAllMyTrackProjects(int userId) {
        // 1) 파라미터 검증
        if (userId <= 0) {
            throw new IllegalArgumentException("유효하지 않은 유저 ID입니다.");
        }

        try {
            log.info("[MyPage] 트랙 프로젝트 조회 시작 - userId={}", userId);

            // 2) DB 조회
            List<MyTrackProject> result = myPageMapper.selectAllMyTrackProjects(userId);

            // 3) 팀원 목록 세팅
            for (MyTrackProject trackProject : result) {
                int trackTeamId = trackProject.getTrackTeamId();

             List<String> members = myPageMapper.selectTrackTeamMembers(trackTeamId);
             trackProject.setTeamMembers(members);

            }

            log.info("[MyPage] 트랙 프로젝트 조회 성공 - userId={}, projectCount={}", userId, result.size());
            return result;

        } catch (Exception e) {
            log.error("[MyPage] 트랙 프로젝트 조회 실패 - userId={}, 오류: {}", userId, e.getMessage(), e);
            throw new RuntimeException("나의 트랙 프로젝트 조회 중 오류가 발생했습니다.", e);
        }
    }

    @Override
    public List<MyFinaleProject> getAllMyFinaleProjects(int userId) {
        // 1) 파라미터 검증
        if (userId <= 0) {
            throw new IllegalArgumentException("유효하지 않은 유저 ID입니다.");
        }

        try {
            log.info("[MyPage] 파이널 프로젝트 조회 시작 - userId={}", userId);

            // 2) DB 조회
            List<MyFinaleProject> result = myPageMapper.selectAllMyFinaleProjects(userId);

            log.info("[MyPage] 파이널 프로젝트 조회 성공 - userId={}, projectCount={}", userId, result.size());
            return result;

        } catch (Exception e) {
            log.error("[MyPage] 파이널 프로젝트 조회 실패 - userId={}, 오류: {}", userId, e.getMessage(), e);
            throw new RuntimeException("나의 파이널 프로젝트 조회 중 오류가 발생했습니다.", e);
        }
    }

    @Override
    public List<MyAlgorithmSolution> getAllMyAlgorithms(int userId) {
        // 1) 파라미터 검증
        if (userId <= 0) {
            throw new IllegalArgumentException("유효하지 않은 유저 ID입니다.");
        }

        try {
            log.info("[MyPage] 알고리즘 풀이 조회 시작 - userId={}", userId);

            // 2) DB 조회
            List<MyAlgorithmSolution> result = myPageMapper.selectAllMyAlgorithmSolutions(userId);

            log.info("[MyPage] 알고리즘 풀이 조회 성공 - userId={}, solutionCount={}", userId, result.size());
            return result;

        } catch (Exception e) {
            log.error("[MyPage] 알고리즘 풀이 조회 실패 - userId={}, 오류: {}", userId, e.getMessage(), e);
            throw new RuntimeException("나의 알고리즘 풀이 조회 중 오류가 발생했습니다.", e);
        }
    }
}
