package com.honeyboard.api.algorithm.solution.service;

import com.honeyboard.api.algorithm.solution.model.AlgorithmSolution;
import com.honeyboard.api.common.response.PageResponse;

import java.util.List;

public interface AlgorithmSolutionService {

	void addAlgorithmSolution(AlgorithmSolution algorithmSolution);
	PageResponse<AlgorithmSolution> getAllAlgorithmSolution(int problemId, Integer generationId, List<String> languages, String sortType, int page, int userId);
	AlgorithmSolution getAlgorithmSolution(int solutionId);
	void updateAlgorithmSolution(AlgorithmSolution algorithmSolution);
	void softDeleteAlgorithmSolution(int solutionId, int userId);
}
