package com.honeyboard.api.web.recommend.mapper;

import com.honeyboard.api.common.model.CreateResponse;
import com.honeyboard.api.web.recommend.model.request.WebRecommendRequest;
import com.honeyboard.api.web.recommend.model.response.WebRecommendDetail;
import com.honeyboard.api.web.recommend.model.response.WebRecommendList;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WebRecommendMapper {
    
    // 웹추천 전체 조회 (검색어 x) 
    List<WebRecommendList> selectAllWebRecommend(@Param("generationId") int generationId, @Param("offset") int offset, @Param("pageSize") int pageSize);
    
    // 웹추천 전체 조회 (검색어 - 제목 O)
    List<WebRecommendList> searchWebRecommendByTitle(@Param("title") String title, @Param("generationId") int generationId,  @Param("offset") int offset, @Param("pageSize") int pageSize);
    
    // 웹추천 상세 조회
    WebRecommendDetail selectWebRecommendById(@Param("recommendId")int recommendId, @Param("userId")int userId);

    // 웹추천 작성
    int insertWebRecommend(@Param("webRecommend") WebRecommendRequest webRecommend, @Param("userId") int userId, @Param("generationId") int generationId,
                           @Param("createResponse") CreateResponse createResponse);

    // 웹추천 수정
    int updateWebRecommend(@Param("recommendId") int recommendId, @Param("webRecommend") WebRecommendRequest webRecommend, @Param("userId") int userId);

    // 웹추천 삭제
    int deleteWebRecommend(int recommendId);

    // 웹추천 개수
    int countWebRecommend(int generationId);

    // 웹추천 검색 개수
    int countSearchWebRecommend(@Param("title") String title, @Param("generationId") int generationId);

    // Url 중복검사 - 생성
    boolean existByUrl(@Param("url") String url, @Param("generationId") int generationId);

    // URL 중복검사 - 수정
    boolean existByUrlforUpdate(@Param("url") String url, @Param("recommendId") int recommendId);

}
