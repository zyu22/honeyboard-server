package com.honeyboard.api.algorithm.language.controller;

import com.honeyboard.api.algorithm.language.model.LanguageList;
import com.honeyboard.api.algorithm.language.service.ProgrammingLanguageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/v1/algorithm/language")
@RequiredArgsConstructor
@Slf4j
public class ProgrammingLanguageController {

    private final ProgrammingLanguageService programmingLanguageService;

    //전체 프로그래밍 언어 조회
    @GetMapping
    public ResponseEntity<?> getAllProgrammingLanguages() {
        log.info("프로그래밍 언어 전체 조회 시작");

        List<LanguageList> languages = programmingLanguageService.getAllProgrammingLanguage();

        log.info("프로그래밍 언어 전체 조회 완료 - 총 언어 수: {}", languages.size());
        return ResponseEntity.ok(languages);
    }
}
