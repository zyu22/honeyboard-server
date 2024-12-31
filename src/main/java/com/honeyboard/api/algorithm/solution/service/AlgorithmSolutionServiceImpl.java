package com.honeyboard.api.algorithm.solution.service;

import com.honeyboard.api.algorithm.solution.mapper.AlgorithmSolutionMapper;
import com.honeyboard.api.algorithm.solution.model.AlgorithmSolution;
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
	
	private final AlgorithmSolutionMapper algorithmSolutionMapper;

	@Override
	public void addAlgorithmSolution(AlgorithmSolution algorithmSolution) {
		log.info("알고리즘 솔루션 추가 시작 - 문제 ID: {}", algorithmSolution.getProblemId());

		if (algorithmSolution == null) {
			throw new IllegalArgumentException("알고리즘 솔루션 정보가 없습니다.");
		}

		int result = algorithmSolutionMapper.insertAlgorithmSolution(algorithmSolution);

		if (result != 1) {
			throw new IllegalArgumentException("알고리즘 솔루션 추가에 실패했습니다.");
		}

		log.info("알고리즘 솔루션 추가 완료 - 문제 ID: {}", algorithmSolution.getProblemId());
	}

	@Override
	public PageResponse<AlgorithmSolution> getAllAlgorithmSolution(int problemId, Integer generationId, List<String> languages,
																			   String sortType, int page, int userId) {
		log.info("알고리즘 솔루션 전체 조회 시작 - 문제 ID: {}, 기수: {}, 페이지: {}", problemId, generationId, page);

		// 전체 풀이 수
	    int totalElements = algorithmSolutionMapper.countAlgorithmSolutions(problemId, generationId, languages, userId);
	    
	    // PageInfo(pageSize = 9)
	    PageInfo pageInfo = new PageInfo(page, 9, totalElements);
	    int offset = (page - 1) * 9;
	    
	    //전체 조회 -> List
	    List<AlgorithmSolution> solutions = algorithmSolutionMapper.selectAllAlgorithmSolution(problemId, generationId, languages, sortType, offset, userId);
		log.info("알고리즘 솔루션 전체 조회 완료 - 총 솔루션 수: {}", totalElements);

	    return new PageResponse<>(solutions, pageInfo);
	}

	@Override
	public AlgorithmSolution getAlgorithmSolution(int solutionId) {
		log.info("알고리즘 솔루션 상세 조회 시작 - 솔루션 ID: {}", solutionId);

		AlgorithmSolution solution = algorithmSolutionMapper.selectAlgorithmSolution(solutionId);

		if (solution == null) {
			log.error("알고리즘 솔루션 조회 실패 - 존재하지 않는 솔루션 ID: {}", solutionId);
			throw new IllegalArgumentException("해당 알고리즘 솔루션을 찾을 수 없습니다.");
		}
		log.info("알고리즘 솔루션 상세 조회 완료");

		return solution;
	}

	@Override
	public void updateAlgorithmSolution(AlgorithmSolution algorithmSolution) {
		log.info("알고리즘 솔루션 수정 시작 - 솔루션 ID: {}", algorithmSolution.getSolutionId());

		if (algorithmSolution == null) {
			throw new IllegalArgumentException("알고리즘 솔루션 정보가 없습니다.");
		}

		int result = algorithmSolutionMapper.updateAlgorithmSolution(algorithmSolution);

		if (result != 1) {
			log.error("알고리즘 솔루션 수정 실패 - 솔루션 ID: {}", algorithmSolution.getSolutionId());
			throw new IllegalArgumentException("알고리즘 솔루션 수정에 실패했습니다.");
		}
		log.info("알고리즘 솔루션 수정 완료");
	}

	@Override
	public void softDeleteAlgorithmSolution(int solutionId, int userId) {
		log.info("알고리즘 솔루션 삭제 시작 - 솔루션 ID: {}, 사용자 ID: {}", solutionId, userId);

		int result = algorithmSolutionMapper.deleteAlgorithmSolution(solutionId, userId);

		if (result != 1) {
			log.error("알고리즘 솔루션 삭제 실패 - 솔루션 ID: {}, 사용자 ID: {}", solutionId, userId);
			throw new IllegalArgumentException("알고리즘 솔루션 삭제에 실패했습니다.");
		}

		log.info("알고리즘 솔루션 삭제 완료");
	}

}
