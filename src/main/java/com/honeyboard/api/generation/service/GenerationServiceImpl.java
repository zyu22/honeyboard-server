package com.honeyboard.api.generation.service;

import com.honeyboard.api.generation.mapper.GenerationMapper;
import com.honeyboard.api.generation.model.Generation;
import com.honeyboard.api.user.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GenerationServiceImpl implements GenerationService {
    private final GenerationMapper generationMapper;

    @Override
    public List<Generation> getAllGenerations() {
        log.info("전체 기수 조회 시작");
        List<Generation> generations = generationMapper.selectGenerations();
        log.info("전체 기수 조회 완료");
        return generations;
    }

    @Override
    public List<User> getUserByGeneration(int generationId) {
        log.info("기수별 학생 조회 시작");
        List<User> users = generationMapper.selectUserByGeneration(generationId);
        return users;
    }
}
