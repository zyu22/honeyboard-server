//package com.honeyboard.api.algorithm.solution.controller;
//
//import com.honeyboard.api.algorithm.solution.model.AlgorithmSolution;
//import com.honeyboard.api.algorithm.solution.model.AlgorithmSolutionResponse;
//import com.honeyboard.api.algorithm.solution.service.AlgorithmSolutionService;
//import com.honeyboard.api.common.response.PageResponse;
//import com.honeyboard.api.user.model.CurrentUser;
//import com.honeyboard.api.user.model.User;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Map;
//
//
//@RestController
//@RequestMapping("/api/v1/algorithm/problem")
//@RequiredArgsConstructor
//@Slf4j
//public class AlgorithmSolutionController {
//
//	private final AlgorithmSolutionService algorithmSolutionService;
//
//	//알고리즘 풀이 작성 POST /api/v1/algorithm/problem/{problemId}/solution..
//	@PostMapping("/{problemId}/solution")
//	public ResponseEntity<?> createAlgorithmSolution(
//			@PathVariable int problemId,
//			@RequestBody AlgorithmSolution algorithmSolution,
//			@CurrentUser User user) {
//		log.info("알고리즘 풀이 작성 요청 - 문제 ID: {}", problemId);
//
//		int userId = user.getUserId();
//		int generationId = user.getGenerationId();
//
//		algorithmSolution.setUserId(userId);
//		algorithmSolution.setGenerationId(generationId);
//		algorithmSolution.setProblemId(problemId);
//
//		algorithmSolutionService.addAlgorithmSolution(algorithmSolution);
//		log.info("알고리즘 풀이 작성 완료 - 문제 ID: {}", problemId);
//
//		int solutionId = algorithmSolution.getSolutionId();
//		return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("solutionId", solutionId));
//	}
//
//	//알고리즘 풀이 전체 조회 GET /api/v1/algorithm/problem/{problemId}/solution
//	@GetMapping("/{problemId}/solution")
//	public ResponseEntity<?> getAllAlgorithmSolution(
//			@PathVariable int problemId,
//			@RequestParam(value = "generationId", defaultValue = "0") int generationId,
//			@RequestParam(value = "language", required = false) List<String> language,
//			@RequestParam(value = "sortType", defaultValue = "latest") String sortType,
//			@RequestParam(value = "currentPage", defaultValue = "1") int currentPage,
//			@CurrentUser User user) {
//		log.info("알고리즘 풀이 전체 조회 요청 - 문제 ID: {}, 페이지: {}", problemId, currentPage);
//
//		int userId = user.getUserId();
//
//		PageResponse<AlgorithmSolutionResponse> pageResponse = algorithmSolutionService.getAllAlgorithmSolution(
//				problemId, generationId, language, sortType, currentPage, userId);
//
//		log.info("알고리즘 풀이 전체 조회 완료 - 총 솔루션 수: {}", pageResponse.getContent().size());
//		return ResponseEntity.ok(pageResponse);
//	}
//
//	//알고리즘 풀이 상세 조회 GET /api/v1/algorithm/problem/{problemId}/solution/{solutionId}
//	@GetMapping("/{problemId}/solution/{solutionId}")
//	public ResponseEntity<?> getAlgorithmSolution(
//			@PathVariable int problemId,
//			@PathVariable int solutionId) {
//		log.info("알고리즘 풀이 상세 조회 요청 - 솔루션 ID: {}", solutionId);
//
//		AlgorithmSolutionResponse solution = algorithmSolutionService.getAlgorithmSolution(solutionId);
//
//		log.info("알고리즘 풀이 상세 조회 완료");
//		return ResponseEntity.ok(solution);
//	}
//
//	//알고리즘 풀이 수정 PUT /api/v1/algorithm/problem/{problemId}/solution/{solutionId}
//	@PutMapping("/{problemId}/solution/{solutionId}")
//	public ResponseEntity<?> updateAlgorithmSolution(
//			@PathVariable int problemId,
//			@PathVariable int solutionId,
//			@RequestBody AlgorithmSolution algorithmSolution,
//			@CurrentUser User user) {
//		log.info("알고리즘 풀이 수정 요청 - 솔루션 ID: {}", solutionId);
//
//		int userId = user.getUserId();
//
//		algorithmSolution.setSolutionId(solutionId);
//		algorithmSolution.setUserId(userId);
//
//		algorithmSolutionService.updateAlgorithmSolution(algorithmSolution);
//
//		log.info("알고리즘 풀이 수정 완료");
//		return ResponseEntity.ok().build();
//	}
//
//	//알고리즘 풀이 삭제 DELETE /api/v1/algorithm/problem/{problemId}/solution/{solutionId}
//	@DeleteMapping("/{problemId}/solution/{solutionId}")
//	public ResponseEntity<?> deleteAlgorithmSolution(
//			@PathVariable int solutionId) {
//		log.info("알고리즘 풀이 삭제 요청 - 솔루션 ID: {}", solutionId);
//
//
//		algorithmSolutionService.softDeleteAlgorithmSolution(solutionId);
//
//		log.info("알고리즘 풀이 삭제 완료");
//		return ResponseEntity.ok().build();
//	}
//}
