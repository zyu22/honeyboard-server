package com.honeyboard.api.generation.controller;

import com.honeyboard.api.generation.model.GenerationList;
import com.honeyboard.api.generation.service.GenerationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/generation")
@RequiredArgsConstructor
@Slf4j
public class GenerationController {
    private final GenerationService generationService;

    @GetMapping
    public ResponseEntity<?> getAllGenerations() {
        log.info("기수 전체 조회 요청");
        List<GenerationList> generations = generationService.getAllGenerations();
        if(generations.isEmpty() || generations.size() == 0) {
            log.info("기수 전체 조회 완료, 값 없음");
            return ResponseEntity.noContent().build();
        }

        log.info("기수 전체 조회 완료");
        return ResponseEntity.status(HttpStatus.OK).body(generations);
    }
}
