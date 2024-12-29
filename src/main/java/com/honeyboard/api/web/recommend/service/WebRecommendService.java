package com.honeyboard.api.web.recommend.service;

import com.honeyboard.api.common.response.PageResponse;
import com.honeyboard.api.web.recommend.model.WebRecommend;

public interface WebRecommendService {
    PageResponse<WebRecommend> getAllWebRecommend(Integer generationId, int currentPage);
    PageResponse<WebRecommend> searchWebRecommend(String title, Integer generationId, int currentPage);
    WebRecommend getWebRecommend(int recommendId);
    boolean addWebRecommend(WebRecommend webRecommend);
    boolean updateWebRecommend(int recommendId, WebRecommend webRecommend);
    boolean deleteWebRecommend(int recommendId);
}
