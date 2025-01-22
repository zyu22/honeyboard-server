package com.honeyboard.api.web.recommend.service;

import com.honeyboard.api.common.model.CreateResponse;
import com.honeyboard.api.common.response.PageResponse;
import com.honeyboard.api.web.recommend.model.request.WebRecommendRequest;
import com.honeyboard.api.web.recommend.model.response.WebRecommendDetail;
import com.honeyboard.api.web.recommend.model.response.WebRecommendList;

public interface WebRecommendService {

    // 웹추천 전체 조회 (검색어 x)
    PageResponse<WebRecommendList> getAllWebRecommend(Integer generationId, int currentPage, int pageSize);

    // 웹추천 전체 조회 (검색어 - 제목 O)
    PageResponse<WebRecommendList> searchWebRecommend(String title, Integer generationId, int currentPage, int pageSize);

    // 웹추천 상세 조회
    WebRecommendDetail getWebRecommend(int recommendId);

    // 웹추천 작성
    CreateResponse addWebRecommend(WebRecommendRequest webRecommend, int userId, int generationId);

    // 웹추천 수정
    void updateWebRecommend(int recommendId, WebRecommendRequest webRecommend, int userId);

    // 웹추천 삭제
    void deleteWebRecommend(int recommendId);
}