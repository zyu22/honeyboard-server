package com.honeyboard.api.algorithm.solution.controller;

import com.honeyboard.api.algorithm.solution.model.request.AlgorithmSolutionRequest;
import com.honeyboard.api.algorithm.solution.model.response.AlgorithmSolutionDetail;
import com.honeyboard.api.algorithm.solution.model.response.AlgorithmSolutionList;
import com.honeyboard.api.algorithm.solution.service.AlgorithmSolutionService;
import com.honeyboard.api.common.model.CreateResponse;
import com.honeyboard.api.common.response.PageResponse;
import com.honeyboard.api.user.model.CurrentUser;
import com.honeyboard.api.user.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/algorithm/problem")
@RequiredArgsConstructor
@Slf4j
public class AlgorithmSolutionController {

	private final AlgorithmSolutionService algorithmSolutionService;

	//알고리즘 풀이 전체 조회 GET /api/v1/algorithm/problem/{problemId}/solution
	@GetMapping("/{problemId}/solution")
	public ResponseEntity<?> getAllAlgorithmSolution(
			@PathVariable int problemId,
			@RequestParam(value = "generationId", defaultValue = "0") int generationId,
			@RequestParam(value = "language", required = false) List<String> language,
			@RequestParam(value = "sortType", defaultValue = "latest") String sortType,
			@RequestParam(value = "currentPage", defaultValue = "1") int currentPage,
			@RequestParam(value = "pageSize", defaultValue = "9") int pageSize) {
		log.info("알고리즘 풀이 전체 조회 요청 - 문제 ID: {}, 페이지: {}", problemId, currentPage);

		PageResponse<AlgorithmSolutionList> response = algorithmSolutionService.getAllAlgorithmSolution(
				problemId, generationId, language, sortType, currentPage, pageSize);

		log.info("알고리즘 풀이 전체 조회 완료 - 총 솔루션 수: {}", response.getContent().size());
		return ResponseEntity.ok(response);
	}

	//알고리즘 풀이 상세 조회 GET /api/v1/algorithm/problem/{problemId}/solution/{solutionId}
	@GetMapping("/{problemId}/solution/{solutionId}")
	public ResponseEntity<?> getAlgorithmSolution(
			@PathVariable int problemId,
			@PathVariable int solutionId) {
		log.info("알고리즘 풀이 상세 조회 요청 - 솔루션 ID: {}", solutionId);

		AlgorithmSolutionDetail solution = algorithmSolutionService.getAlgorithmSolution(solutionId);

		log.info("알고리즘 풀이 상세 조회 완료");
		return ResponseEntity.ok(solution);
	}

	//알고리즘 풀이 작성 POST /api/v1/algorithm/problem/{problemId}/solution..
	@PostMapping("/{problemId}/solution")
	public ResponseEntity<?> createAlgorithmSolution(
			@PathVariable int problemId,
			@RequestBody AlgorithmSolutionRequest algorithmSolution,
			@CurrentUser User user) {
		log.info("알고리즘 풀이 작성 요청 - 문제 ID: {}", problemId);

		int userId = user.getUserId();
		int generationId = user.getGenerationId();

		CreateResponse createResponse = algorithmSolutionService.addAlgorithmSolution(problemId, algorithmSolution, userId, generationId);
		log.info("알고리즘 풀이 작성 완료 - 문제 ID: {}", problemId);

		return ResponseEntity.status(HttpStatus.CREATED).body(createResponse);
	}

	//알고리즘 풀이 수정 PUT /api/v1/algorithm/problem/{problemId}/solution/{solutionId}
	@PutMapping("/{problemId}/solution/{solutionId}")
	public ResponseEntity<?> updateAlgorithmSolution(
			@PathVariable int problemId,
			@PathVariable int solutionId,
			@RequestBody AlgorithmSolutionRequest algorithmSolution) {
		log.info("알고리즘 풀이 수정 요청 - 솔루션 ID: {}", solutionId);

		algorithmSolutionService.updateAlgorithmSolution(solutionId, algorithmSolution);

		log.info("알고리즘 풀이 수정 완료");
		return ResponseEntity.ok().build();
	}

	//알고리즘 풀이 삭제 DELETE /api/v1/algorithm/problem/{problemId}/solution/{solutionId}
	@DeleteMapping("/{problemId}/solution/{solutionId}")
	public ResponseEntity<?> deleteAlgorithmSolution(
			@PathVariable int solutionId) {
		log.info("알고리즘 풀이 삭제 요청 - 솔루션 ID: {}", solutionId);

		algorithmSolutionService.softDeleteAlgorithmSolution(solutionId);

		log.info("알고리즘 풀이 삭제 완료");
		return ResponseEntity.ok().build();
	}
}
