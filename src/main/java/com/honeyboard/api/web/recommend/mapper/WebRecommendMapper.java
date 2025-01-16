package com.honeyboard.api.web.recommend.mapper;

import com.honeyboard.api.web.recommend.model.request.WebRecommendRequest;
import com.honeyboard.api.web.recommend.model.response.WebRecommendDetail;
import com.honeyboard.api.web.recommend.model.response.WebRecommendList;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WebRecommendMapper {
    
    // 웹추천 전체 조회 (검색어 x) 
    List<WebRecommendList> selectAllWebRecommend(Integer generationId, @Param("offset") int offset, @Param("pageSize") int pageSize);
    
    // 웹추천 전체 조회 (검색어 - 제목 O)
    List<WebRecommendList> searchWebRecommendByTitle(String title, Integer generationId,  @Param("offset") int offset, @Param("pageSize") int pageSize);
    
    // 웹추천 상세 조회
    WebRecommendDetail selectWebRecommendById(int recommendId);

    // 웹추천 작성
    int insertWebRecommend(@Param("webRecommend") WebRecommendRequest webRecommend, @Param("userId") int userId);

    // 웹추천 수정
    int updateWebRecommend(@Param("recommendId") int recommendId, @Param("webRecommend")WebRecommendRequest webRecommend, @Param("userId") int userId);

    // 웹추천 삭제
    int deleteWebRecommend(int recommendId);

    // 웹추천 개수
    int countWebRecommend(Integer generationId);

    // 웹추천 검색 개수
    int countSearchWebRecommend(String title, Integer generationId);

    // Url 중복검사
    boolean existByUrl(String url);

}
