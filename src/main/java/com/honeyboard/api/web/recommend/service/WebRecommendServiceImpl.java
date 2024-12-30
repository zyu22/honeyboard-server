package com.honeyboard.api.web.recommend.service;

import com.honeyboard.api.common.model.PageInfo;
import com.honeyboard.api.common.response.PageResponse;
import com.honeyboard.api.web.recommend.mapper.WebRecommendMapper;
import com.honeyboard.api.web.recommend.model.WebRecommend;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WebRecommendServiceImpl implements WebRecommendService {
    private  final WebRecommendMapper webRecommendMapper;

    @Override
    public PageResponse<WebRecommend> getAllWebRecommend(Integer generationId, int currentPage) {
        int totalElements = webRecommendMapper.countWebRecommend(generationId);
        PageInfo pageInfo = new PageInfo(currentPage, 16, totalElements);
        int offset = (currentPage-1)*16;

        List<WebRecommend> webRecommends = webRecommendMapper.selectAllWebRecommend(generationId, offset);
        return new PageResponse<>(webRecommends, pageInfo);
    }

    @Override
    public PageResponse<WebRecommend> searchWebRecommend(String title, Integer generationId, int currentPage) {
        int totalElements = webRecommendMapper.countSearchWebRecommend(title, generationId);
        PageInfo pageInfo = new PageInfo(currentPage, 16, totalElements);
        int offset = (currentPage-1)*16;

        List<WebRecommend> webRecommends = webRecommendMapper.searchWebRecommendByTitle(title, generationId, currentPage);
        return new PageResponse<>(webRecommends, pageInfo);
    }

    @Override
    public WebRecommend getWebRecommend(int recommendId) {
        return webRecommendMapper.selectWebRecommendById(recommendId);
    }

    @Override
    public boolean addWebRecommend(WebRecommend webRecommend) {
        int result = webRecommendMapper.insertWebRecommend(webRecommend);
        return result==1;
    }

    @Override
    public boolean updateWebRecommend(int recommendId, WebRecommend webRecommend) {
        int result = webRecommendMapper.updateWebRecommend(recommendId, webRecommend);
        return result==1;
    }

    @Override
    public boolean deleteWebRecommend(int recommendId) {
        int result = webRecommendMapper.deleteWebRecommend(recommendId);
        return result==1;
    }
}
