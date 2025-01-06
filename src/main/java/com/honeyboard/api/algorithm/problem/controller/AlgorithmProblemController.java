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
            @RequestParam(required = true) String searchType,
            @RequestParam(required = false) String keyword) {
        log.info("알고리즘 문제 조회 요청 - 페이지: {}, 사이즈: {}, 검색타입: {}, 키워드: {}",
                page, size, searchType, keyword);

        PageResponse<AlgorithmProblem> list = (searchType != null && !searchType.isEmpty())
                ? as.searchProblem(page, size, searchType, keyword)
                : as.getAllProblem(page, size);

        return list.getContent().isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(list);
    }

    @GetMapping("/{problemId}")
    public ResponseEntity<?> getAlgorithmProblem(@PathVariable int problemId) {
        AlgorithmProblem problem = as.getProblem(problemId);
        return problem == null ? ResponseEntity.noContent().build() : ResponseEntity.ok(problem);
    }

    @PostMapping
    public ResponseEntity<?> createAlgorithmProblem(@RequestBody AlgorithmProblem ap) {
        log.info("알고리즘 문제 생성 요청 - 제목: {}", ap.getTitle());

        AlgorithmProblem createdProblem = as.addProblem(ap);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProblem);
    }

    @PutMapping("/{problemId}")
    public ResponseEntity<?> updateAlgorithmProblem(@PathVariable int problemId, @RequestBody AlgorithmProblem ap) {
        log.info("알고리즘 문제 수정 요청 - ID: {}", problemId);

        AlgorithmProblem updatedProblem = as.updateProblem(problemId, ap);
        return ResponseEntity.ok(updatedProblem);
    }

    @DeleteMapping("/{problemId}")
    public ResponseEntity<?> deleteAlgorithmProblem(@PathVariable int problemId) {
        log.info("알고리즘 문제 삭제 요청 - ID: {}", problemId);

        as.softDeleteProblem(problemId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/check-url")
    public ResponseEntity<?> checkAlgorithmProblemUrl(@RequestParam String url) {
        log.info("URL 중복 체크 요청: {}", url);

        boolean res = as.existsByUrl(url);
        return ResponseEntity.ok().body(res);
    }


}
