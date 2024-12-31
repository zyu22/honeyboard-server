package com.honeyboard.api.web.recommend.mapper;

import com.honeyboard.api.web.recommend.model.WebRecommend;

import java.util.List;

public interface WebRecommendMapper {
    List<WebRecommend> selectAllWebRecommend(Integer generationId, int offset, int pageSize);
    List<WebRecommend> searchWebRecommendByTitle(String title, Integer generationId, int offset, int pageSize);
    WebRecommend selectWebRecommendById(int recommendId);
    int insertWebRecommend(WebRecommend webRecommend);
    int updateWebRecommend(WebRecommend webRecommend);
    int deleteWebRecommend(int recommendId);
    int countWebRecommend(Integer generationId);
    int countSearchWebRecommend(String title, Integer generationId);
}
