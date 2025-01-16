package com.honeyboard.api.algorithm.solution.service;

import com.honeyboard.api.algorithm.problem.mapper.AlgorithmProblemMapper;
import com.honeyboard.api.algorithm.solution.mapper.AlgorithmSolutionMapper;
import com.honeyboard.api.algorithm.solution.model.request.AlgorithmSolutionRequest;
import com.honeyboard.api.algorithm.solution.model.response.AlgorithmSolutionDetail;
import com.honeyboard.api.algorithm.solution.model.response.AlgorithmSolutionList;
import com.honeyboard.api.common.model.CreateResponse;
import com.honeyboard.api.common.model.PageInfo;
import com.honeyboard.api.common.response.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlgorithmSolutionServiceImpl implements AlgorithmSolutionService {
	
	private final AlgorithmSolutionMapper asm;
	private final AlgorithmProblemMapper apm;

	@Override
	public PageResponse<AlgorithmSolutionList> getAllAlgorithmSolution(int problemId, int generationId, List<String> languages,
																	   String sortType, int currentPage, int pageSize) {
		log.info("알고리즘 솔루션 전체 조회 시작 - 문제 ID: {}, 기수: {}, 페이지: {}", problemId, generationId, currentPage);
		if (currentPage <= 0 || pageSize <= 0) {
			throw new IllegalArgumentException("페이지 번호와 크기는 1 이상이어야 합니다.");
		}

		// 전체 풀이 수
	    int totalElement = asm.countAlgorithmSolutions(problemId, generationId, languages);

	    // PageInfo(pageSize = 9)
		PageInfo pageInfo = new PageInfo(currentPage, pageSize, totalElement);
	    int offset = (currentPage - 1) * pageSize;

		if (totalElement > 0 && offset >= totalElement) {
			throw new IllegalArgumentException("요청하신 페이지가 전체 페이지 범위를 초과했습니다.");
		}

	    //전체 조회 -> List
	    List<AlgorithmSolutionList> solutions = asm.selectAllProblemSolutions(problemId, generationId, languages, sortType, offset, pageSize);
		log.info("알고리즘 솔루션 전체 조회 완료 - 총 솔루션 수: {}", totalElement);

		return new PageResponse<>(solutions, pageInfo);
	}

	@Override
	public AlgorithmSolutionDetail getAlgorithmSolution(int solutionId) {
		log.info("알고리즘 솔루션 상세 조회 시작 - 솔루션 ID: {}", solutionId);

		AlgorithmSolutionDetail solution = asm.selectAlgorithmSolution(solutionId);

		if (solution == null) {
			log.error("알고리즘 솔루션 조회 실패 - 존재하지 않는 솔루션 ID: {}", solutionId);
			throw new IllegalArgumentException("해당 알고리즘 솔루션을 찾을 수 없습니다.");
		}
		log.info("알고리즘 솔루션 상세 조회 완료");

		return solution;
	}

	@Override
	public CreateResponse addAlgorithmSolution(int problemId, AlgorithmSolutionRequest request, int userId, int generationId) {
		log.info("알고리즘 솔루션 추가 시작 - 문제 title: {}", request.getTitle());

		if (request == null) {
			throw new IllegalArgumentException("알고리즘 솔루션 정보가 없습니다.");
		}

		// 문제 존재 여부 확인
		if (apm.selectAlgorithmProblem(problemId) == null) {
			log.error("존재하지 않는 문제 - ID: {}", problemId);
			throw new IllegalArgumentException("존재하지 않는 문제입니다.");
		}

		CreateResponse createResponse = new CreateResponse();
		int result = asm.insertAlgorithmSolution(problemId, request, userId, generationId, createResponse);

		if (result != 1) {
			log.error("풀이 생성 실패 - 제목: {}", request.getTitle());
			throw new IllegalArgumentException("알고리즘 솔루션 추가에 실패했습니다.");
		}

		log.info("알고리즘 솔루션 추가 완료 - 문제 title: {}", request.getTitle());
		return createResponse;
	}

	@Override
	public void updateAlgorithmSolution(int solutionId, AlgorithmSolutionRequest request) {
		log.info("알고리즘 솔루션 수정 시작 - 솔루션 ID: {}", solutionId);

		if (request == null) {
			throw new IllegalArgumentException("알고리즘 솔루션 정보가 없습니다.");
		}

		int result = asm.updateAlgorithmSolution(solutionId, request);

		if (result != 1) {
			log.error("알고리즘 솔루션 수정 실패 - 솔루션 ID: {}", solutionId);
			throw new IllegalArgumentException("알고리즘 솔루션 수정에 실패했습니다.");
		}
		log.info("알고리즘 솔루션 수정 완료");
	}

	@Override
	public void softDeleteAlgorithmSolution(int solutionId) {
		log.info("알고리즘 솔루션 삭제 시작 - 솔루션 ID: {}", solutionId);

		if (solutionId <= 0) {
			log.error("잘못된 풀이 ID - ID: {}", solutionId);
			throw new IllegalArgumentException("유효하지 않은 풀이 ID입니다.");
		}

		int result = asm.deleteAlgorithmSolution(solutionId);

		if (result != 1) {
			log.error("알고리즘 솔루션 삭제 실패 - 솔루션 ID: {}", solutionId);
			throw new IllegalArgumentException("알고리즘 솔루션 삭제에 실패했습니다.");
		}

		log.info("알고리즘 솔루션 삭제 완료 - ID: {}", solutionId);
	}

}
