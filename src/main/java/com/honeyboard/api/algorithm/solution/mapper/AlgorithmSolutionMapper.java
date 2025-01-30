package com.honeyboard.api.algorithm.solution.mapper;

import com.honeyboard.api.algorithm.solution.model.request.AlgorithmSolutionRequest;
import com.honeyboard.api.algorithm.solution.model.response.AlgorithmSolutionDetail;
import com.honeyboard.api.algorithm.solution.model.response.AlgorithmSolutionList;
import com.honeyboard.api.common.model.CreateResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AlgorithmSolutionMapper {

    // AlgorithmSolution 전체조회
	List<AlgorithmSolutionList> selectAllProblemSolutions(@Param("problemId") int problemId,
                                                       @Param("generationId") int generationId,
                                                       @Param("languages") List<String> languages,
                                                       @Param("sortType") String sortType,
                                                       @Param("offset") int offset,
                                                       @Param("pageSize") int pageSize);

	// AlgorithmSolution 상세조회
	AlgorithmSolutionDetail selectAlgorithmSolution(@Param("solutionId") int solutionId,
													@Param("userId") int userId);
	// AlgorithmSolution 작성
	int insertAlgorithmSolution(@Param("problemId") int problemId,
								@Param("algorithmSolution") AlgorithmSolutionRequest algorithmSolution,
								@Param("userId") int userId,
								@Param("generationId") int generationId,
								@Param("createResponse") CreateResponse response);
	int updateAlgorithmSolution(@Param("solutionId") int solutionId,
								@Param("algorithmSolution") AlgorithmSolutionRequest algorithmSolution,
								@Param("userId") int userId,
								@Param("role") String role);
	int deleteAlgorithmSolution(@Param("solutionId") int solutionId,
								@Param("userId") int userId,
								@Param("role") String role);

	//페이지네이션
	int countAlgorithmSolutions(@Param("problemId") int problemId,
								@Param("generationId") int generationId,
								@Param("languages") List<String> languages);

}
