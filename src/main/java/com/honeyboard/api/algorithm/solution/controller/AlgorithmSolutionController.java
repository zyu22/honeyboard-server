package com.honeyboard.api.algorithm.solution.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.honeyboard.api.algorithm.solution.model.AlgorithmSolution;
import com.honeyboard.api.algorithm.solution.service.AlgorithmSolutionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/v1/algorithm/problem")
@RequiredArgsConstructor
@Slf4j
public class AlgorithmSolutionController {

	private final AlgorithmSolutionService algorithmSolutionService;
	
	//알고리즘 풀이 작성 POST /api/v1/algorithm/problem/{problemId}/solution
	@PostMapping("/{problemId}/solution")
	public ResponseEntity<?> createAlgorithmSolution(
			@PathVariable int problemId, 
			@RequestBody AlgorithmSolution algorithmSolution) {
		try {
			boolean result = algorithmSolutionService.addAlgorithmSolution(problemId, algorithmSolution);
			
			if(result) {
				return new ResponseEntity<>(HttpStatus.CREATED);
			}
			else {
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			log.error("알고리즘 풀이 작성 에러: ", e);
	        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//알고리즘 풀이 전체 조회 GET /api/v1/algorithm/problem/{problemId}/solution
	@GetMapping("/{problemId}/solution")
	public ResponseEntity<?> getAllAlgorithmSolution(
			@PathVariable int problemId, 
			@RequestParam(value = "generationId") int generationId,
			@RequestParam(value = "language") List<String> language,
			@RequestParam(value = "sortType", defaultValue = "latest") String sortType,
			@RequestParam(value = "page", defaultValue = "1") int page) {
		try {
			List<AlgorithmSolution> solutions = algorithmSolutionService.getAllAlgorithmSolution(
	                problemId, generationId, language, sortType, page);
			
			if (solutions == null) {
	            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	        }
	        
	        if (solutions.isEmpty()) {
	            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	        }
	        
	        return new ResponseEntity<>(solutions, HttpStatus.OK);
		} catch (Exception e) {
			log.error("알고리즘 풀이 전체 조회 에러: ", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	} 
	
	//알고리즘 풀이 상세 조회 GET /api/v1/algorithm/problem/{problemId}/solution/{solutionId}
	@GetMapping("/{problemId}/solution/{solutionId}")
	public ResponseEntity<?> getAlgorithmSolution(
			@PathVariable int problemId, 
			@PathVariable int solutionId) {
		try {
			AlgorithmSolution solution = algorithmSolutionService.getAlgorithmSolution(solutionId);
			
			if (solution == null) {
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			}
			
			if (solution.isDeleted()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(solution, HttpStatus.OK);
			
		} catch (Exception e) {
			log.error("알고리즘 풀이 상세 조회 에러: ", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	//알고리즘 풀이 수정 PUT /api/v1/algorithm/problem/{problemId}/solution/{solutionId}
	@PutMapping("/{problemId}/solution/{solutionId}")
	public ResponseEntity<?> updateAlgorithmSolution(
			@PathVariable int problemId, 
			@PathVariable int solutionId,
			@RequestBody AlgorithmSolution algorithmSolution) {
		try {
			boolean result = algorithmSolutionService.updateAlgorithmSolution(algorithmSolution);
			
			if(result) {
				return new ResponseEntity<>(HttpStatus.OK);
			}
			else {
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			log.error("알고리즘 풀이 수정 에러: ", e);
	        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//알고리즘 풀이 삭제 DELETE /api/v1/algorithm/problem/{problemId}/solution/{solutionId}
	@DeleteMapping("/{problemId}/solution/{solutionId}")
	public ResponseEntity<?> deleteAlgorithmSolution(
			@PathVariable int problemId, 
			@PathVariable int solutionId) {
		try {
			boolean result = algorithmSolutionService.softDeleteAlgorithmSolution(solutionId);
			
			if(result) {
				return new ResponseEntity<>(HttpStatus.OK);
			}
			else {
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			log.error("알고리즘 풀이 삭제 에러: ", e);
	        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
