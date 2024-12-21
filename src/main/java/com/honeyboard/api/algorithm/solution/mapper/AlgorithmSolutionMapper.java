package com.honeyboard.api.algorithm.solution.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.honeyboard.api.algorithm.solution.model.AlgorithmSolution;

@Mapper
public interface AlgorithmSolutionMapper {

	Integer insertAlgorithmSolution(int problemId, AlgorithmSolution algorithmSolution);
	List<AlgorithmSolution> selectAllAlgorithmSolution(int problemId, int generationId, List<String> languages, String sortType, int page);
	AlgorithmSolution selectAlgorithmSolution(int solutionId);
	Integer updateAlgorithmSolution(AlgorithmSolution algorithmSolution);
	Integer deleteAlgorithmSolution(int solutionId);
	
}
