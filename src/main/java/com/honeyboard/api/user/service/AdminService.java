package com.honeyboard.api.user.service;


import com.honeyboard.api.generation.model.GenerationList;
import com.honeyboard.api.user.model.UserInfo;

import java.util.List;

public interface AdminService {

    List<UserInfo> getUserByGeneration(Integer generationId);

    UserInfo getUserByUserId(int userId);

    void updateUser(int userId, UserInfo userInfo);

    int addGeneration(GenerationList generation);

    void updateGenerationIsActive(int generationId);

    void removeGeneration(int generationId);
}
