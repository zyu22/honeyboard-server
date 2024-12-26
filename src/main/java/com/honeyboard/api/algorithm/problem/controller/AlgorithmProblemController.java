package com.honeyboard.api.algorithm.problem.controller;

import com.honeyboard.api.algorithm.problem.model.AlgorithmProblem;
import com.honeyboard.api.algorithm.problem.service.AlgorithmProblemService;
import com.honeyboard.api.common.response.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/algorithm/problem")
@RequiredArgsConstructor
@Slf4j
public class AlgorithmProblemController {
    private final AlgorithmProblemService as;

    @GetMapping
    public ResponseEntity<?> getAllAlgorithmProblem(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "16") int size,
            @RequestParam(required = false) String searchType,
            @RequestParam(required = false) String keyword
    ) {
        try {
            PageResponse<AlgorithmProblem> list;
            log.info("알고리즘 문제 조회 요청 - 페이지: {}, 사이즈: {}, 검색타입: {}, 키워드: {}",
                    page, size, searchType, keyword);

            if (searchType != null && !searchType.isEmpty() && keyword != null && !keyword.trim().isEmpty()) {
                list = as.searchProblem(page, size, searchType, keyword);
            } else {
                list = as.getAllProblem(page, size);
            }

            if (list.getContent().isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok().body(list);
        } catch (Exception e) {
            log.error("알고리즘 문제 조회 중 에러 발생", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createAlgorithmProblem(@RequestBody AlgorithmProblem ap) {
        try {
            if (as.existsByUrl(ap.getUrl())) {
                log.warn("중복된 URL로 문제 생성 시도: {}", ap.getUrl());
                return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 존재하는 문제입니다.");
            }
            log.info("알고리즘 문제 생성 시작 - 제목: {}", ap.getTitle());
            AlgorithmProblem createdProblem = as.addProblem(ap);
            log.info("알고리즘 문제 생성 완료 - ID: {}", createdProblem.getId());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(createdProblem);
        } catch (Exception e) {
            log.error("알고리즘 문제 생성 중 에러 발생", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{problemId}")
    public ResponseEntity<?> updateAlgorithmProblem(@PathVariable int problemId, @RequestBody AlgorithmProblem ap) {
        try {
            if (!as.existsById(problemId)) {
                log.warn("존재하지 않는 문제 수정 시도 - ID: {}", problemId);
                return ResponseEntity.notFound().build();
            }

            log.info("알고리즘 문제 수정 시작 - ID: {}", problemId);
            AlgorithmProblem updatedProblem = as.updateProblem(problemId, ap);
            log.info("알고리즘 문제 수정 완료 - ID: {}", problemId);
            return ResponseEntity.ok(updatedProblem);

        } catch (Exception e) {
            log.error("알고리즘 문제 수정 중 에러 발생 - ID: {}", problemId, e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{problemId}")
    public ResponseEntity<?> deleteAlgorithmProblem(@PathVariable int problemId) {
        try {
            if (!as.existsById(problemId)) {
                log.warn("존재하지 않는 문제 삭제 시도 - ID: {}", problemId);
                return ResponseEntity.notFound().build();
            }
            log.info("알고리즘 문제 삭제 시작 - ID: {}", problemId);
            boolean res = as.softDeleteProblem(problemId);
            if (res) {
                log.info("알고리즘 문제 삭제 완료 - ID: {}", problemId);
                return ResponseEntity.status(HttpStatus.OK).build();
            }
            throw new Exception("문제 삭제에 실패했습니다.");
        } catch (Exception e) {
            log.error("알고리즘 문제 삭제 중 에러 발생 - ID: {}", problemId, e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/check-url")
    public ResponseEntity<?> checkAlgorithmProblemUrl(@RequestBody String url) {
        try {
            log.info("URL 중복 체크 요청: {}", url);
            boolean res = as.existsByUrl(url);
            return ResponseEntity.ok().body(res);
        } catch (Exception e) {
            log.error("URL 중복 체크 중 에러 발생: {}", url, e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
