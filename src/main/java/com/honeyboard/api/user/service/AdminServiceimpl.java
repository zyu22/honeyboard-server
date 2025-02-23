package com.honeyboard.api.user.service;

import com.honeyboard.api.exception.BusinessException;
import com.honeyboard.api.exception.ErrorCode;
import com.honeyboard.api.generation.model.GenerationList;
import com.honeyboard.api.user.mapper.AdminMapper;
import com.honeyboard.api.user.model.UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminServiceimpl implements AdminService {

    private final AdminMapper adminMapper;

    @Override
    public List<UserInfo> getUserByGeneration(Integer generationId) {
        log.info("기수별 유저 조회 시작 - 기수 ID: {}", generationId);
        List<UserInfo> userInfo = adminMapper.selectUserByGeneration(generationId);
        log.info("기수별 유저 조회 성공 - 기수 ID: {}", generationId);
        return userInfo;
    }

    @Override
    public UserInfo getUserByUserId(int userId) {
        log.info("유저 상세정보 조회 시작 - 유저 ID: {}", userId);
        UserInfo userInfo = adminMapper.selectUserById(userId);

        if (userInfo == null) {
            throw new IllegalArgumentException("존재하지 않는 유저입니다.");
        }
        log.info("유저 상세정보 조회 완료 - 유저 ID: {}", userId);
        return userInfo;
    }

    @Override
    public void updateUser(int userId, UserInfo userInfo) {
        log.info("유저 정보 변경 시작 - 유저 ID: {}", userId);
        int result = adminMapper.updateUserInfo(userInfo, userId);

        if (result <= 0) {
            throw new IllegalArgumentException("유저 정보 변경에 실패하였습니다.");
        }

    }

    @Override
    public int addGeneration(GenerationList generation) {
        log.info("기수 등록 시작 - 기수 번호: {}", generation.getName());
        int result = adminMapper.insertGeneration(generation);

        if (result <= 0) {
            throw new IllegalArgumentException("기수 등록에 실패하였습니다.");
        }

        log.info("기수 등록 완료 - 기수: {}, ID: {}", generation.getName(), generation.getId());
        return generation.getId();
    }

    @Override
    public void updateGenerationIsActive(int generationId) {
        log.info("기수 활성화 변경 시작 - 기수 ID: {}", generationId);
        int result = adminMapper.updateGenerationIsActive(generationId);

        if (result <= 0) {
            throw new IllegalArgumentException("기수 활성화 변경에 실패했습니다.");
        }

        log.info("기수 활성화 변경 성공 - 기수 ID: {}", generationId);
    }

    @Override
    public void removeGeneration(int generationId) {
        log.info("기수 삭제 시작 - 기수 ID: {}", generationId);

        // 유저 존재하는지 확인
        int count = adminMapper.selectUserByGenerationId(generationId);
        if(count > 0) {
            log.info("해당 기수에 유저 존재");
            throw new BusinessException(ErrorCode.GENERATION_DELETE_RESTRICTED);
        }

        int result = adminMapper.deleteGenerationById(generationId);

        if (result <= 0) {
            throw new IllegalArgumentException("기수 삭제에 실패하였습니다.");
        }

        log.info("기수 삭제 성공 - 기수 ID: {}", generationId);
    }

}
