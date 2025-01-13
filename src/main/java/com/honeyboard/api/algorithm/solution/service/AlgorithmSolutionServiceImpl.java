package com.honeyboard.api.algorithm.solution.service;

import com.honeyboard.api.algorithm.solution.mapper.AlgorithmSolutionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlgorithmSolutionServiceImpl implements AlgorithmSolutionService {
	
	private final AlgorithmSolutionMapper algorithmSolutionMapper;

//	@Override
//	public void addAlgorithmSolution(AlgorithmSolution algorithmSolution) {
//		log.info("알고리즘 솔루션 추가 시작 - 문제 ID: {}", algorithmSolution.getProblemId());
//
//		if (algorithmSolution == null) {
//			throw new IllegalArgumentException("알고리즘 솔루션 정보가 없습니다.");
//		}
//
//		int result = algorithmSolutionMapper.insertAlgorithmSolution(algorithmSolution);
//
//		if (result != 1) {
//			throw new IllegalArgumentException("알고리즘 솔루션 추가에 실패했습니다.");
//		}
//
//		log.info("알고리즘 솔루션 추가 완료 - 문제 ID: {}", algorithmSolution.getProblemId());
//	}
//
//	@Override
//	public PageResponse<AlgorithmSolutionResponse> getAllAlgorithmSolution(int problemId, int generationId, List<String> languages,
//																			   String sortType, int currentPage, int userId) {
//		log.info("알고리즘 솔루션 전체 조회 시작 - 문제 ID: {}, 기수: {}, 페이지: {}", problemId, generationId, currentPage);
//
//		// 전체 풀이 수
//	    int totalElements = algorithmSolutionMapper.countAlgorithmSolutions(problemId, generationId, languages, userId);
//
//	    // PageInfo(pageSize = 9)
//	    PageInfo pageInfo = new PageInfo(currentPage, 9, totalElements);
//	    int offset = (currentPage - 1) * 9;
//
//	    //전체 조회 -> List
//	    List<AlgorithmSolutionResponse> solutions = algorithmSolutionMapper.selectAllAlgorithmSolution(problemId, generationId, languages, sortType, offset, userId);
//		log.info("알고리즘 솔루션 전체 조회 완료 - 총 솔루션 수: {}", totalElements);
//
//	    return new PageResponse<>(solutions, pageInfo);
//	}
//
//	@Override
//	public AlgorithmSolutionResponse getAlgorithmSolution(int solutionId) {
//		log.info("알고리즘 솔루션 상세 조회 시작 - 솔루션 ID: {}", solutionId);
//
//		AlgorithmSolutionResponse solution = algorithmSolutionMapper.selectAlgorithmSolution(solutionId);
//
//		if (solution == null) {
//			log.error("알고리즘 솔루션 조회 실패 - 존재하지 않는 솔루션 ID: {}", solutionId);
//			throw new IllegalArgumentException("해당 알고리즘 솔루션을 찾을 수 없습니다.");
//		}
//		log.info("알고리즘 솔루션 상세 조회 완료");
//
//		return solution;
//	}
//
//	@Override
//	public void updateAlgorithmSolution(AlgorithmSolution algorithmSolution) {
//		log.info("알고리즘 솔루션 수정 시작 - 솔루션 ID: {}", algorithmSolution.getSolutionId());
//
//		if (algorithmSolution == null) {
//			throw new IllegalArgumentException("알고리즘 솔루션 정보가 없습니다.");
//		}
//
//		int result = algorithmSolutionMapper.updateAlgorithmSolution(algorithmSolution);
//
//		if (result != 1) {
//			log.error("알고리즘 솔루션 수정 실패 - 솔루션 ID: {}", algorithmSolution.getSolutionId());
//			throw new IllegalArgumentException("알고리즘 솔루션 수정에 실패했습니다.");
//		}
//		log.info("알고리즘 솔루션 수정 완료");
//	}
//
//	@Override
//	public void softDeleteAlgorithmSolution(int solutionId) {
//		log.info("알고리즘 솔루션 삭제 시작 - 솔루션 ID: {}", solutionId);
//
//		int result = algorithmSolutionMapper.deleteAlgorithmSolution(solutionId);
//
//		if (result != 1) {
//			log.error("알고리즘 솔루션 삭제 실패 - 솔루션 ID: {}", solutionId);
//			throw new IllegalArgumentException("알고리즘 솔루션 삭제에 실패했습니다.");
//		}
//
//		log.info("알고리즘 솔루션 삭제 완료");
//	}

}
