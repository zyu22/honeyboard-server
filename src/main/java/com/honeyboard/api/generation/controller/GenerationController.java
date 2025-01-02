package com.honeyboard.api.generation.controller;

import com.honeyboard.api.generation.model.Generation;
import com.honeyboard.api.generation.service.GenerationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.honeyboard.api.user.model.User;

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
        List<Generation> generations = generationService.getAllGenerations();
        if(generations.isEmpty() || generations.size() == 0) {
            log.info("기수 전체 조회 완료, 값 없음");
            return ResponseEntity.noContent().build();
        }

        log.info("기수 전체 조회 완료");
        return ResponseEntity.status(HttpStatus.OK).body(generations);
    }

    @GetMapping("/{generationId}")
    public ResponseEntity<?> getGenerationById(@PathVariable("generationId") int generationId) {
        log.info("기수별 학생 조회 요청 - 기수: {}", generationId);
        List<User> users = generationService.getUserByGeneration(generationId);

        if(users.isEmpty() || users.size() == 0) {
            log.info("기수별 학생 조회 완료, 값 없음");
            return ResponseEntity.noContent().build();
        }

        log.info("기수별 학생 조회 완료 - 기수: {}", generationId);
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }
}
