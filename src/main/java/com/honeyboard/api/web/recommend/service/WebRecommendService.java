package com.honeyboard.api.web.recommend.service;

import com.honeyboard.api.common.response.PageResponse;
import com.honeyboard.api.web.recommend.model.WebRecommend;

public interface WebRecommendService {
    PageResponse<WebRecommend> getAllWebRecommend(Integer generationId, int currentPage, int pageSize);
    PageResponse<WebRecommend> searchWebRecommend(String title, Integer generationId, int currentPage, int pageSize);
    WebRecommend getWebRecommend(int recommendId);
    void addWebRecommend(WebRecommend webRecommend);
    void updateWebRecommend(int recommendId, WebRecommend webRecommend);
    void deleteWebRecommend(int recommendId);
}