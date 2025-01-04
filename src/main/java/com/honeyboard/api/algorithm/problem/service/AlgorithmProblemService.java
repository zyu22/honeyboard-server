package com.honeyboard.api.algorithm.problem.service;

import com.honeyboard.api.algorithm.problem.model.AlgorithmProblem;
import com.honeyboard.api.common.response.PageResponse;

public interface AlgorithmProblemService {
    PageResponse<AlgorithmProblem> getAllProblem(int currentPage, int pageSize);

    PageResponse<AlgorithmProblem> searchProblem(int currentPage, int pageSize, String searchType, String keyword);

    AlgorithmProblem getProblem(int problemId);

    AlgorithmProblem addProblem(AlgorithmProblem algorithmProblem);

    AlgorithmProblem updateProblem(int problemId, AlgorithmProblem algorithmProblem);

    boolean softDeleteProblem(int problemId);

    boolean existsByUrl(String url);

    boolean existsById(int id);


}
