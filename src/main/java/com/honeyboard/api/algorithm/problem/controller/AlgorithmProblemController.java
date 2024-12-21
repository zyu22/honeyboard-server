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
            @RequestParam(defaultValue = "6") int size,
            @RequestParam(required = false) String searchType,
            @RequestParam(required = false) String keyword
    ) {
        try {
            PageResponse<AlgorithmProblem> list;
            if (searchType == null || searchType.isEmpty()) {
                list = as.getAllProblem();
            } else {
                list = as.searchProblem(searchType, keyword);
            }
            return ResponseEntity.ok().body(list);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createAlgorithmProblem(@RequestBody AlgorithmProblem ap) {
        try {
            if (as.existsByUrl(ap.getUrl())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            int res = as.addProblem(ap);
            if (res == 1) {
                return ResponseEntity.status(HttpStatus.CREATED).build();
            }
            throw new Exception();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{problemId}")
    public ResponseEntity<?> updateAlgorithmProblem(@PathVariable int problemId, @RequestBody AlgorithmProblem ap) {
        try {
            if (!as.existsById(problemId)) {
                return ResponseEntity.notFound().build();
            }

            int res = as.updateProblem(problemId, ap);
            if (res == 1) {
                return ResponseEntity.status(HttpStatus.OK).build();
            }
            throw new Exception();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{problemId}")
    public ResponseEntity<?> deleteAlgorithmProblem(@PathVariable int problemId) {
        try {
            if (!as.existsById(problemId)) {
                return ResponseEntity.notFound().build();
            }
            int res = as.softDeleteProblem(problemId);
            if (res == 1) {
                return ResponseEntity.status(HttpStatus.OK).build();
            }
            throw new Exception();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/check-url")
    public ResponseEntity<?> checkAlgorithmProblemUrl(@RequestBody String url) {
        try {
            boolean res = as.existsByUrl(url);
            return ResponseEntity.ok().body(res);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
