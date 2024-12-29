package com.honeyboard.api.web.guide.service;

import com.honeyboard.api.common.model.PageInfo;
import com.honeyboard.api.common.response.PageResponse;
import com.honeyboard.api.web.guide.mapper.WebGuideMapper;
import com.honeyboard.api.web.guide.model.WebGuide;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WebGuideServiceImpl implements WebGuideService {
    private final WebGuideMapper webGuideMapper;

    @Override
    public PageResponse<WebGuide> getAllWebGuide(Integer generationId, int currentPage) {
        int totalElements = webGuideMapper.countWebGuide(generationId);
        PageInfo pageInfo = new PageInfo(currentPage, 8, totalElements);
        int offset = (currentPage-1)*8;

        List<WebGuide> webGuides = webGuideMapper.selectAllWebGuide(generationId, offset);
        return new PageResponse<>(webGuides, pageInfo);
    }

    @Override
    public PageResponse<WebGuide> searchWebGuide(String title, Integer generationId, int currentPage) {
        int totalElements = webGuideMapper.countSearchWebGuide(title, generationId);
        PageInfo pageInfo = new PageInfo(currentPage, 8, totalElements);
        int offset = (currentPage-1)*8;

        List<WebGuide> webGuides = webGuideMapper.searchWebGuideByTitle(title, generationId, offset);
        return new PageResponse<>(webGuides, pageInfo);
    }

    @Override
    public WebGuide getWebGuide(int guideId) {
        return webGuideMapper.selectWebGuideById(guideId);
    }

    @Override
    public boolean addWebGuide(WebGuide webGuide) {
        int result = webGuideMapper.insertWebGuide(webGuide);
        return result==1;
    }

    @Override
    public boolean updateWebGuide(int guideId, WebGuide webGuide) {
        int result = webGuideMapper.updateWebGuide(guideId, webGuide);
        return result==1;
    }

    @Override
    public boolean deleteWebGuide(int guideId) {
        int result = webGuideMapper.deleteWebGuide(guideId);
        return result==1;
    }
}