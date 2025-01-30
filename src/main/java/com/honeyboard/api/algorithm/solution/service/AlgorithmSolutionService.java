package com.honeyboard.api.algorithm.solution.service;

import com.honeyboard.api.algorithm.solution.model.request.AlgorithmSolutionRequest;
import com.honeyboard.api.algorithm.solution.model.response.AlgorithmSolutionDetail;
import com.honeyboard.api.algorithm.solution.model.response.AlgorithmSolutionList;
import com.honeyboard.api.common.model.CreateResponse;
import com.honeyboard.api.common.response.PageResponse;

import java.util.List;

public interface AlgorithmSolutionService {

    // Alogorithm Solution 전체조회
	PageResponse<AlgorithmSolutionList> getAllAlgorithmSolution(int problemId, int generationId, List<String> languages, String sortType, int currentPage, int pageSize);
    //Alogorithm Solution 상세조회
    AlgorithmSolutionDetail getAlgorithmSolution(int solutionId, int userId);
    //Alogorithm Solution 작성
    CreateResponse addAlgorithmSolution(int problemId, AlgorithmSolutionRequest algorithmSolution, int userId, int generationId);
	//Alogorithm Solution 수정
    void updateAlgorithmSolution(int solutionId, AlgorithmSolutionRequest algorithmSolution, int userId, String role);
	//Algorithm Solution 삭제
    void softDeleteAlgorithmSolution(int solutionId, int userId, String role);
}
