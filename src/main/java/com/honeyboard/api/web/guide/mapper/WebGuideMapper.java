package com.honeyboard.api.web.guide.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.honeyboard.api.common.model.CreateResponse;
import com.honeyboard.api.web.guide.model.request.WebGuideRequest;
import com.honeyboard.api.web.guide.model.response.WebGuideDetail;
import com.honeyboard.api.web.guide.model.response.WebGuideList;

public interface WebGuideMapper {
    List<WebGuideList> selectAllWebGuide(@Param("generationId") int generationId, @Param("offset") int offset, @Param("pageSize") int pageSize);
    List<WebGuideList> searchWebGuideByTitle(@Param("generationId") int generationId, @Param("offset") int offset, @Param("pageSize") int pageSize, String title);
    WebGuideDetail selectWebGuideById(int guideId);
    int createWebGuide(@Param("webGuideRequest") WebGuideRequest webGuideRequest, @Param("userId") int userId, @Param("createResponse") CreateResponse createResponse);
    int updateWebGuide(@Param("guideId") int guideId, @Param("webGuideRequest")WebGuideRequest webGuideRequest);
    int softDeleteWebGuide(int guideId);
    int countWebGuide(int generationId);
    int countSearchWebGuide(@Param("title") String title, @Param("generationId") int generationId);
}
