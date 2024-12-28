package com.honeyboard.api.web.guide.mapper;

import com.honeyboard.api.web.guide.model.WebGuide;

import java.util.List;

public interface WebGuideMapper {
    List<WebGuide> selectAllWebGuide(Integer generationId, int offset);
    List<WebGuide> searchWebGuideByTitle(String title, Integer generationId, int offset);
    WebGuide selectWebGuideById(int guideId);
    int insertWebGuide(WebGuide webRecommend);
    int updateWebGuide(int guideId, WebGuide webRecommend);
    int deleteWebGuide(int guideId);
    int countWebGuide(Integer generationId);
    int countSearchWebGuide(String title, Integer generationId);
}
