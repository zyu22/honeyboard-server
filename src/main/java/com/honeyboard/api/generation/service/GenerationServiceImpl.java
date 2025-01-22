package com.honeyboard.api.generation.service;

import com.honeyboard.api.generation.mapper.GenerationMapper;
import com.honeyboard.api.generation.model.GenerationList;
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
    public List<GenerationList> getAllGenerations() {
        log.info("전체 기수 조회 시작");
        List<GenerationList> generations = generationMapper.selectGenerations();
        log.info("전체 기수 조회 완료");
        return generations;
    }
}
