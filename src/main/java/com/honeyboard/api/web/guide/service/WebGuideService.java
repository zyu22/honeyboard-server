package com.honeyboard.api.web.guide.service;

import com.honeyboard.api.common.response.PageResponse;
import com.honeyboard.api.web.guide.model.WebGuide;

public interface WebGuideService {
    PageResponse<WebGuide> getAllWebGuide(Integer generationId, int currentPage, int pageSize);
    PageResponse<WebGuide> searchWebGuide(String title, Integer generationId, int currentPage, int pageSize);
    WebGuide getWebGuide(int guideId);
    void addWebGuide(WebGuide webGuide);
    void updateWebGuide(int guideId, WebGuide webGuide);
    void deleteWebGuide(int guideId);
}