package com.honeyboard.api.algorithm.tag.mapper;

import com.honeyboard.api.algorithm.tag.model.response.TagResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TagMapper {

    // 전체 Tag 조회
    //List<TagResponse> selectAllTag();

    // Tag 검색
    List<TagResponse> selectSearchTag(@Param("keyword") String keyword);

    // Tag 추가
    int insertTag(TagResponse tag);

    // Tag 검색
    TagResponse selectTagByName(String name);

    // Algorithm Problem에 해당하는 tag 리스트 반환
    List<TagResponse> selectProblemTags(@Param("problemId") int problemId);

    // Algorithm Problem의 태그 관리
    // Algorithm Problem 태그 추가
    int insertAlgorithmProblemTags(@Param("problemId") int problemId,
                                   @Param("tagIds") List<Integer> tagIds);
    // Algorithm Problem 태그 id 반환
    List<Integer> getTagIdsByProblemId(@Param("problemId") int problemId);
    // Algorithm Problem 태그 삭제
    int deleteSpecificAlgorithmProblemTags(@Param("problemId") int problemId,
                                           @Param("tagIds") List<Integer> tagIds);
}
