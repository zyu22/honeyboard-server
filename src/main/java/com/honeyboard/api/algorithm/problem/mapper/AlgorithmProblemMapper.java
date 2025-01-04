package com.honeyboard.api.algorithm.problem.mapper;

import com.honeyboard.api.algorithm.problem.model.AlgorithmProblem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AlgorithmProblemMapper {
    // 조회 관련
    List<AlgorithmProblem> selectAllAlgorithmProblem(@Param("offset") int offset, @Param("pageSize") int pageSize);

    List<AlgorithmProblem> selectSearchAlgorithmProblem(@Param("offset") int offset, @Param("pageSize") int pageSize,
                                                        @Param("searchType") String searchType, @Param("keyword") String keyword);

    AlgorithmProblem selectAlgorithmProblem(@Param("problemId") int problemId);

    // 문제 CUD
    int insertAlgorithmProblem(AlgorithmProblem algorithmProblem);

    int updateAlgorithmProblem(@Param("problemId") int problemId, @Param("algorithmProblem") AlgorithmProblem algorithmProblem);

    int deleteAlgorithmProblem(@Param("problemId") int problemId);

    // 태그 관계 관리
    int insertAlgorithmProblemTags(@Param("problemId") int problemId, @Param("tagIds") List<Integer> tagIds);
    

    // 존재 여부 확인
    int existsByUrl(@Param("url") String url);

    int existsById(@Param("id") int id);

    // 카운트
    int countAlgorithmProblem();

    int countSearchAlgorithmProblem(@Param("searchType") String searchType, @Param("keyword") String keyword);

    List<Integer> getTagIdsByProblemId(@Param("problemId") int problemId);

    int deleteSpecificAlgorithmProblemTags(@Param("problemId") int problemId, @Param("tagIds") List<Integer> tagIds);
}