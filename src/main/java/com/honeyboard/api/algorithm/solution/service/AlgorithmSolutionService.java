package com.honeyboard.api.algorithm.solution.service;

import java.util.List;

import com.honeyboard.api.algorithm.solution.model.AlgorithmSolution;
import com.honeyboard.api.common.response.PageResponse;

public interface AlgorithmSolutionService {

	boolean addAlgorithmSolution(int problemId, AlgorithmSolution algorithmSolution);
	PageResponse<AlgorithmSolution> getAllAlgorithmSolution(int problemId, Integer generationId, List<String> languages, String sortType, int page);
	AlgorithmSolution getAlgorithmSolution(int solutionId);
	boolean updateAlgorithmSolution(AlgorithmSolution algorithmSolution);
	boolean softDeleteAlgorithmSolution(int solutionId);
}
