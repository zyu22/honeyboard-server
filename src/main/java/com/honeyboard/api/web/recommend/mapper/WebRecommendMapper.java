package com.honeyboard.api.web.recommend.mapper;

import com.honeyboard.api.web.recommend.model.WebRecommend;

import java.util.List;

public interface WebRecommendMapper {
    List<WebRecommend> selectAllWebRecommend(Integer generationId, int offset);
    List<WebRecommend> searchWebRecommendByTitle(String title, Integer generationId, int offset);
    WebRecommend selectWebRecommendById(int recommendId);
    int insertWebRecommend(WebRecommend webRecommend);
    int updateWebRecommend(int recommendId, WebRecommend webRecommend);
    int deleteWebRecommend(int recommendId);
    int countWebRecommend(Integer generationId);
    int countSearchWebRecommend(String title, Integer generationId);
}
