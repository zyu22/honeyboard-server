package com.honeyboard.api.web.guide.mapper;

import com.honeyboard.api.web.guide.model.WebGuide;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WebGuideMapper {
    List<WebGuide> selectAllWebGuide(Integer generationId, @Param("offset") int offset, @Param("pageSize") int pageSize);
    List<WebGuide> searchWebGuideByTitle(String title, Integer generationId, @Param("offset") int offset, @Param("pageSize") int pageSize);
    WebGuide selectWebGuideById(int guideId);
    int insertWebGuide(WebGuide webGuide);
    int updateWebGuide(@Param("guideId") int guideId, @Param("webGuide")WebGuide webGuide);
    int deleteWebGuide(int guideId);
    int countWebGuide(Integer generationId);
    int countSearchWebGuide(String title, Integer generationId);
}
