package com.honeyboard.api.algorithm.problem.mapper;

import com.honeyboard.api.algorithm.problem.model.request.AlgorithmProblemRequest;
import com.honeyboard.api.algorithm.problem.model.response.AlgorithmProblemDetail;
import com.honeyboard.api.algorithm.problem.model.response.AlgorithmProblemList;
import com.honeyboard.api.algorithm.solution.model.response.AlgorithmSolutionList;
import com.honeyboard.api.algorithm.tag.model.TagResponse;
import com.honeyboard.api.common.model.CreateResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AlgorithmProblemMapper {

    // AlgorithmProblem 전체조회
    List<AlgorithmProblemList> selectAllAlgorithmProblem(@Param("offset") int offset,
                                                         @Param("pageSize") int pageSize,
                                                         @Param("searchType") String searchType,
                                                         @Param("keyword") String keyword);
    // AlgorithmProblem 전체조회 카운트
    int countAlgorithmProblem(@Param("searchType") String searchType,
                              @Param("keyword") String keyword);

    // AlgorithmProblem 상세조회
    AlgorithmProblemDetail selectProblemBasicInfo(@Param("problemId") int problemId);
    List<AlgorithmSolutionList> selectProblemSolutions(@Param("problemId") int problemId);
    List<TagResponse> selectProblemTags(@Param("problemId") int problemId);

    // AlgorithmProblem 작성
    int insertAlgorithmProblem(@Param("request") AlgorithmProblemRequest request,
                               @Param("userId") int userId,
                               @Param("createResponse") CreateResponse response);
    int existsByUrl(@Param("request") AlgorithmProblemRequest request);

    // AlgorithmProblem 수정
    int updateAlgorithmProblem(@Param("request") AlgorithmProblemRequest request,
                                @Param("problemId") int problemId);

    // AlgorithmProblem 삭제
    int deleteAlgorithmProblem(@Param("problemId") int problemId);

    // 태그 관계 관리
    int insertAlgorithmProblemTags(@Param("problemId") int problemId,
                                   @Param("tagIds") List<Integer> tagIds);
    List<Integer> getTagIdsByProblemId(@Param("problemId") int problemId);
    int deleteSpecificAlgorithmProblemTags(@Param("problemId") int problemId,
                                           @Param("tagIds") List<Integer> tagIds);

}