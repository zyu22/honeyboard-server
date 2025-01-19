package com.honeyboard.api.user.service;

import com.honeyboard.api.exception.BusinessException;
import com.honeyboard.api.exception.ErrorCode;
import com.honeyboard.api.user.mapper.MyPageMapper;
import com.honeyboard.api.user.model.mypage.MyAlgorithmSolutionList;
import com.honeyboard.api.user.model.mypage.MyFinaleProjectList;
import com.honeyboard.api.user.model.mypage.MyTrackProjectList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MyPageServiceImpl implements MyPageService {

    private final MyPageMapper myPageMapper;

    @Override
    public List<MyTrackProjectList> getAllMyTrackProjects(int userId) {
        // 유효성 검사
        validateUserId(userId);
        log.info("내 관통 프로젝트 조회 시작 - 유저 ID: {}", userId);
        return myPageMapper.selectAllMyTrackProjects(10);
    }

    @Override
    public List<MyFinaleProjectList> getAllMyFinaleProjects(int userId) {
         // 유효성검사
        validateUserId(userId);
        log.info("내 파이널 프로젝트 조회 시작 - 유저 ID: {}", userId);
        return myPageMapper.selectAllMyFinaleProjects(userId);
    }

    @Override
    public List<MyAlgorithmSolutionList> getAllMyAlgorithms(int userId) {
        // 1) 파라미터 검증
        if (userId <= 0) {
            throw new IllegalArgumentException("유효하지 않은 유저 ID입니다.");
        }

        try {
            log.info("[MyPage] 알고리즘 풀이 조회 시작 - userId={}", userId);

            // 2) DB 조회
            List<MyAlgorithmSolutionList> result = myPageMapper.selectAllMyAlgorithmSolutions(userId);

            log.info("[MyPage] 알고리즘 풀이 조회 성공 - userId={}, solutionCount={}", userId, result.size());
            return result;

        } catch (Exception e) {
            log.error("[MyPage] 알고리즘 풀이 조회 실패 - userId={}, 오류: {}", userId, e.getMessage(), e);
            throw new RuntimeException("나의 알고리즘 풀이 조회 중 오류가 발생했습니다.", e);
        }
    }


    private void validateUserId(int userId) {
        if (userId <= 0) {
            throw new BusinessException(ErrorCode.INVALID_ID);
        }
    }
}
