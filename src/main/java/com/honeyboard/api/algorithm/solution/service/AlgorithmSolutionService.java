package com.honeyboard.api.algorithm.solution.service;

import java.util.List;

import com.honeyboard.api.algorithm.solution.model.AlgorithmSolution;

public interface AlgorithmSolutionService {

	boolean addAlgorithmSolution(int problemId, AlgorithmSolution algorithmSolution);
	List<AlgorithmSolution> getAllAlgorithmSolution(int problemId, int generationId, List<String> languages, String sortType, int page);
	AlgorithmSolution getAlgorithmSolution(int solutionId);
	boolean updateAlgorithmSolution(AlgorithmSolution algorithmSolution);
	boolean softDeleteAlgorithmSolution(int solutionId);
}
