package com.honeyboard.api.web.recommend.mapper;

import com.honeyboard.api.web.recommend.model.WebRecommend;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WebRecommendMapper {
    List<WebRecommend> selectAllWebRecommend(Integer generationId,  @Param("offset") int offset, @Param("pageSize") int pageSize);
    List<WebRecommend> searchWebRecommendByTitle(String title, Integer generationId,  @Param("offset") int offset, @Param("pageSize") int pageSize);
    WebRecommend selectWebRecommendById(int recommendId);
    int insertWebRecommend(WebRecommend webRecommend);
    int updateWebRecommend(@Param("recommendId") int recommendId, @Param("webRecommend")WebRecommend webRecommend);
    int deleteWebRecommend(int recommendId);
    int countWebRecommend(Integer generationId);
    int countSearchWebRecommend(String title, Integer generationId);
}
