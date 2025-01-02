package com.honeyboard.api.user.mapper;

import com.honeyboard.api.generation.model.Generation;
import com.honeyboard.api.user.model.UserInfo;

import java.util.List;

public interface AdminMapper {
    List<UserInfo> selectUserByGeneration(Integer generationId);

    UserInfo selectUserById(int userId);

    int updateUserInfo(UserInfo userInfo);

    int insertGeneration(Generation generation);

    int updateGenerationIsActive(int generationId);

    int deleteGenerationById(int generationId);
}
