package com.honeyboard.api.user.mapper;

import com.honeyboard.api.generation.model.GenerationList;
import com.honeyboard.api.user.model.UserInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AdminMapper {
    List<UserInfo> selectUserByGeneration(Integer generationId);

    UserInfo selectUserById(int userId);

    int updateUserInfo(@Param("UserInfo") UserInfo userInfo, @Param("userId") int userId);

    int insertGeneration(GenerationList generation);

    int updateGenerationIsActive(int generationId);

    int deleteGenerationById(int generationId);
}
