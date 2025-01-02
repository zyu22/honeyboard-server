package com.honeyboard.api.generation.mapper;

import com.honeyboard.api.generation.model.Generation;
import com.honeyboard.api.user.model.User;

import java.util.List;

public interface GenerationMapper {
    // 전체 기수 조회
    List<Generation> selectGenerations();

    // 기수별 학생 조회
    List<User> selectUserByGeneration(int generationId);
}
