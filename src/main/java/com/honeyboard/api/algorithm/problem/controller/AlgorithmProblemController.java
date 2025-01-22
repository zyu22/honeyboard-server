package com.honeyboard.api.algorithm.problem.controller;

import com.honeyboard.api.algorithm.problem.model.request.AlgorithmProblemRequest;
import com.honeyboard.api.algorithm.problem.model.response.AlgorithmProblemDetail;
import com.honeyboard.api.algorithm.problem.model.response.AlgorithmProblemList;
import com.honeyboard.api.algorithm.problem.service.AlgorithmProblemService;
import com.honeyboard.api.common.model.CreateResponse;
import com.honeyboard.api.common.response.PageResponse;
import com.honeyboard.api.user.model.CurrentUser;
import com.honeyboard.api.user.model.User;
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

    // AlgorithmProblem 전체조회
    @GetMapping
    public ResponseEntity<?> getAllAlgorithmProblem(
            @RequestParam(defaultValue = "1") int currentPage,
            @RequestParam(defaultValue = "16") int pageSize,
            @RequestParam(required = false) String searchType,
            @RequestParam(required = false) String keyword) {
        log.info("알고리즘 문제 조회 요청 - 페이지: {}, 사이즈: {}, 검색타입: {}, 키워드: {}",
                currentPage, pageSize, searchType, keyword);

        PageResponse<AlgorithmProblemList> response = as.getAllProblem(currentPage, pageSize, searchType, keyword);

        return response.getContent().isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(response);
    }

    // AlgorithmProblem 상세조회
    @GetMapping("/{problemId}")
    public ResponseEntity<?> getAlgorithmProblem(@PathVariable int problemId) {
        log.info("알고리즘 문제 상세 조회 요청 - 문제ID: {}", problemId);

        AlgorithmProblemDetail problemDetail = as.getProblem(problemId);

        log.info("알고리즘 문제 상세 조회 완료 - 문제ID: {}", problemId);

        return ResponseEntity.ok(problemDetail);
    }

    // AlgorithmProblem 문제 작성
    @PostMapping
    public ResponseEntity<?> createAlgorithmProblem(@RequestBody AlgorithmProblemRequest request,
                                                    @CurrentUser User user) {
        log.info("알고리즘 문제 생성 요청 - 제목: {}", request.getTitle());

        CreateResponse createResponse = as.addProblem(request, user.getUserId());
        log.info("알고리즘 문제 작성 완료 - ID: {}", createResponse.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(createResponse);
    }

    // AlgorithmProblem 문제 수정
    @PutMapping("/{problemId}")
    public ResponseEntity<?> updateAlgorithmProblem(@PathVariable int problemId,
                                                    @RequestBody AlgorithmProblemRequest request) {
        log.info("알고리즘 문제 수정 요청 - ID: {}", problemId);

        as.updateProblem(problemId, request);
        return ResponseEntity.ok().build();
    }

    // AlgorithmProblem 문제 삭제
    @DeleteMapping("/{problemId}")
    public ResponseEntity<?> deleteAlgorithmProblem(@PathVariable int problemId) {
        log.info("알고리즘 문제 삭제 요청 - ID: {}", problemId);

        as.softDeleteProblem(problemId);
        return ResponseEntity.ok().build();
    }
}
