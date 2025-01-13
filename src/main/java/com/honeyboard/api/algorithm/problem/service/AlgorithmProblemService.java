package com.honeyboard.api.algorithm.problem.service;

import com.honeyboard.api.algorithm.problem.model.request.AlgorithmProblemRequest;
import com.honeyboard.api.algorithm.problem.model.response.AlgorithmProblemDetail;
import com.honeyboard.api.algorithm.problem.model.response.AlgorithmProblemList;
import com.honeyboard.api.common.model.CreateResponse;
import com.honeyboard.api.common.response.PageResponse;

public interface AlgorithmProblemService {

    // AlgorithmProblem 전체조회
    PageResponse<AlgorithmProblemList> getAllProblem(int currentPage, int pageSize, String searchType, String keyword);

    // AlgorithmProblem 상세조회
    AlgorithmProblemDetail getProblem(int problemId);

    // AlgorithmProblem 추가
    CreateResponse addProblem(AlgorithmProblemRequest request, int userId);

    // AlgorithmProblem 수정
    void updateProblem(int problemId, AlgorithmProblemRequest request);

    // AlgorithmProblem 삭제
    void softDeleteProblem(int problemId);

    // url로 중복 조회
    boolean existsByUrl(String url);

}
