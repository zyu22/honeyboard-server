package com.honeyboard.api.algorithm.solution.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.honeyboard.api.algorithm.solution.model.AlgorithmSolution;

@Mapper
public interface AlgorithmSolutionMapper {

	int insertAlgorithmSolution(int problemId, AlgorithmSolution algorithmSolution);
	List<AlgorithmSolution> selectAllAlgorithmSolution(int problemId, Integer generationId, List<String> languages, String sortType, int offset);
	AlgorithmSolution selectAlgorithmSolution(int solutionId);
	int updateAlgorithmSolution(AlgorithmSolution algorithmSolution);
	int deleteAlgorithmSolution(int solutionId);
	
	//페이지네이션
	int countAlgorithmSolutions(int problemId, Integer generationId, List<String> languages);
	
}
