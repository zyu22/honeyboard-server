package com.honeyboard.api.web.recommend.service;

import com.honeyboard.api.common.response.PageResponse;
import com.honeyboard.api.web.recommend.model.request.WebRecommendRequest;
import com.honeyboard.api.web.recommend.model.response.WebRecommendDetail;
import com.honeyboard.api.web.recommend.model.response.WebRecommendList;

public interface WebRecommendService {
    PageResponse<WebRecommendList> getAllWebRecommend(Integer generationId, int currentPage, int pageSize);
    PageResponse<WebRecommendList> searchWebRecommend(String title, Integer generationId, int currentPage, int pageSize);
    WebRecommendDetail getWebRecommend(int recommendId);
    void addWebRecommend(WebRecommendRequest webRecommend);
    void updateWebRecommend(int recommendId, WebRecommendRequest webRecommend);
    void deleteWebRecommend(int recommendId);
}