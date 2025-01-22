package com.honeyboard.api.web.guide.service;

import com.honeyboard.api.common.model.CreateResponse;
import com.honeyboard.api.common.response.PageResponse;
import com.honeyboard.api.web.guide.model.request.WebGuideRequest;
import com.honeyboard.api.web.guide.model.response.WebGuideDetail;
import com.honeyboard.api.web.guide.model.response.WebGuideList;

public interface WebGuideService {
    PageResponse<WebGuideList> getAllWebGuide(int generationId, int currentPage, int pageSize);
    PageResponse<WebGuideList> searchWebGuide(int generationId, int currentPage, int pageSize, String title);
    WebGuideDetail getWebGuideDetail(int guideId, int userId);
    CreateResponse createWebGuide(WebGuideRequest webGuideRequest, int userId, int generationId);
    void updateWebGuide(int guideId, WebGuideRequest webGuideRequest, int userId);
    void softDeleteWebGuide(int guideId, int userId);
}