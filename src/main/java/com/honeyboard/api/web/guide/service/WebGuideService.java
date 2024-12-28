package com.honeyboard.api.web.guide.service;

import com.honeyboard.api.common.response.PageResponse;
import com.honeyboard.api.web.guide.model.WebGuide;

public interface WebGuideService {
    PageResponse<WebGuide> getAllWebGuide(Integer generationId, int currentPage);
    PageResponse<WebGuide> searchWebGuide(String title, Integer generationId, int currentPage);
    WebGuide getWebGuide(int guideId);
    boolean addWebGuide(WebGuide webGuide);
    boolean updateWebGuide(int guideId, WebGuide webGuide);
    boolean deleteWebGuide(int guideId);
}
