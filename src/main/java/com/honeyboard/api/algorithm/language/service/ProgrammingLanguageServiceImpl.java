package com.honeyboard.api.algorithm.language.service;

import com.honeyboard.api.algorithm.language.mapper.ProgrammingLanguageMapper;
import com.honeyboard.api.algorithm.language.model.ProgrammingLanguage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProgrammingLanguageServiceImpl implements ProgrammingLanguageService{

    private final ProgrammingLanguageMapper programmingLanguageMapper;

    @Override
    public List<ProgrammingLanguage> getAllProgrammingLanguage() {
        log.info("프로그래밍 언어 전체 조회 시작");

        //전체 조회 -> List
        List<ProgrammingLanguage> languages = programmingLanguageMapper.selectAllProgrammingLanguage();
        log.info("프로그래밍 언어 전체 조회 완료 - 총 언어 수: {}", languages.size());

        return languages;
    }
}
