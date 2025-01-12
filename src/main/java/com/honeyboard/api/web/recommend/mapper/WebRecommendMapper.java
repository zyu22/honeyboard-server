package com.honeyboard.api.web.recommend.mapper;

import com.honeyboard.api.web.recommend.model.request.WebRecommendRequest;
import com.honeyboard.api.web.recommend.model.response.WebRecommendDetail;
import com.honeyboard.api.web.recommend.model.response.WebRecommendList;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WebRecommendMapper {
    List<WebRecommendList> selectAllWebRecommend(Integer generationId, @Param("offset") int offset, @Param("pageSize") int pageSize);
    List<WebRecommendList> searchWebRecommendByTitle(String title, Integer generationId,  @Param("offset") int offset, @Param("pageSize") int pageSize);
    WebRecommendDetail selectWebRecommendById(int recommendId);
    int insertWebRecommend(WebRecommendRequest webRecommend);
    int updateWebRecommend(@Param("recommendId") int recommendId, @Param("webRecommend")WebRecommendRequest webRecommend);
    int deleteWebRecommend(int recommendId);
    int countWebRecommend(Integer generationId);
    int countSearchWebRecommend(String title, Integer generationId);
}
