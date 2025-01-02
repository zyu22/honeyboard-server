package com.honeyboard.api.user.mapper;

import com.honeyboard.api.user.model.User;

import java.util.List;

public interface AdminMapper {
    List<User> selectUserByGeneration(Integer generationId);
}
