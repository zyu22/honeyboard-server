package com.honeyboard.api.algorithm.solution.mapper;

import com.honeyboard.api.algorithm.solution.model.AlgorithmSolution;
import com.honeyboard.api.algorithm.solution.model.AlgorithmSolutionResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AlgorithmSolutionMapper {

	int insertAlgorithmSolution(AlgorithmSolution algorithmSolution);
	List<AlgorithmSolutionResponse> selectAllAlgorithmSolution(@Param("problemId") int problemId,
															   @Param("generationId") Integer generationId,
															   @Param("languages") List<String> languages,
															   @Param("sortType") String sortType,
															   @Param("offset") int offset,
															   @Param("userId") int userId);
	AlgorithmSolutionResponse selectAlgorithmSolution(int solutionId);
	int updateAlgorithmSolution(AlgorithmSolution algorithmSolution);
	int deleteAlgorithmSolution(int solutionId);
	
	//페이지네이션
	int countAlgorithmSolutions(@Param("problemId") int problemId,
								@Param("generationId") Integer generationId,
								@Param("languages") List<String> languages,
								@Param("userId") int userId);
	
}
