package com.honeyboard.api.algorithm.solution.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.honeyboard.api.algorithm.solution.mapper.AlgorithmSolutionMapper;
import com.honeyboard.api.algorithm.solution.model.AlgorithmSolution;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AlgorithmSolutionServiceImpl implements AlgorithmSolutionService {
	
	private final AlgorithmSolutionMapper algorithmSolutionMapper;

	@Override
	public boolean addAlgorithmSolution(int problemId, AlgorithmSolution algorithmSolution) {
		int result = algorithmSolutionMapper.insertAlgorithmSolution(problemId, algorithmSolution);
		return result == 1;
	}

	@Override
	public PageResponse<AlgorithmSolution> getAllAlgorithmSolution(int problemId, Integer generationId, List<String> languages,
			String sortType, int page) {
		// 전체 풀이 수
	    int totalElements = algorithmSolutionMapper.countAlgorithmSolutions(problemId, generationId, languages);
	    
	    // PageInfo(pageSize = 9)
	    PageInfo pageInfo = new PageInfo(page, 9, totalElements);
	    int offset = (page - 1) * 9;
	    
	    //전체 조회 -> List
	    List<AlgorithmSolution> solutions = algorithmSolutionMapper.selectAllAlgorithmSolution(problemId, generationId, languages, sortType, offset);
	    return new PageResponse<>(solutions, pageInfo);
	}

	@Override
	public AlgorithmSolution getAlgorithmSolution(int solutionId) {
		return algorithmSolutionMapper.selectAlgorithmSolution(solutionId);
	}

	@Override
	public boolean updateAlgorithmSolution(AlgorithmSolution algorithmSolution) {
		int result = algorithmSolutionMapper.updateAlgorithmSolution(algorithmSolution);
		return result == 1;
	}

	@Override
	public boolean softDeleteAlgorithmSolution(int solutionId) {
		int result = algorithmSolutionMapper.deleteAlgorithmSolution(solutionId);
		return result == 1;
	}

}
