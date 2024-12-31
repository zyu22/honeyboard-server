package com.honeyboard.api.algorithm.solution.mapper;

import com.honeyboard.api.algorithm.solution.model.AlgorithmSolution;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AlgorithmSolutionMapper {

	int insertAlgorithmSolution(AlgorithmSolution algorithmSolution);
	List<AlgorithmSolution> selectAllAlgorithmSolution(@Param("problemId") int problemId,
													   @Param("generationId") Integer generationId,
													   @Param("languages") List<String> languages,
													   @Param("sortType") String sortType,
													   @Param("offset") int offset,
													   @Param("userId") int userId);
	AlgorithmSolution selectAlgorithmSolution(int solutionId);
	int updateAlgorithmSolution(AlgorithmSolution algorithmSolution);
	int deleteAlgorithmSolution(int solutionId, int userId);
	
	//페이지네이션
	int countAlgorithmSolutions(@Param("problemId") int problemId,
								@Param("generationId") Integer generationId,
								@Param("languages") List<String> languages,
								@Param("userId") int userId);
	
}
