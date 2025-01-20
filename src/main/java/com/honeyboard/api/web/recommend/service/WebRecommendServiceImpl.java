package com.honeyboard.api.web.recommend.service;

import com.honeyboard.api.common.model.CreateResponse;
import com.honeyboard.api.common.model.PageInfo;
import com.honeyboard.api.common.response.PageResponse;
import com.honeyboard.api.exception.BusinessException;
import com.honeyboard.api.exception.ErrorCode;
import com.honeyboard.api.web.recommend.mapper.WebRecommendMapper;
import com.honeyboard.api.web.recommend.model.request.WebRecommendRequest;
import com.honeyboard.api.web.recommend.model.response.WebRecommendDetail;
import com.honeyboard.api.web.recommend.model.response.WebRecommendList;
import com.sun.jdi.request.DuplicateRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.honeyboard.api.exception.ErrorCode.DUPLICATE_URL;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebRecommendServiceImpl implements WebRecommendService {
    private final WebRecommendMapper webRecommendMapper;

    @Override
    public PageResponse<WebRecommendList> getAllWebRecommend(Integer generationId, int currentPage, int pageSize) {
        log.info("웹 추천 전체 조회 시작 - 기수: {}", generationId);

        int totalElements = webRecommendMapper.countWebRecommend(generationId);
        PageInfo pageInfo = new PageInfo(currentPage, pageSize, totalElements);
        int offset = (currentPage-1)*pageSize;

        List<WebRecommendList> webRecommends = webRecommendMapper.selectAllWebRecommend(generationId, offset, pageSize);

        log.info("웹 추천 전체 조회 완료 - 조회된 사이트 수: {}", webRecommends.size());
        return new PageResponse<>(webRecommends, pageInfo);
    }

    @Override
    public PageResponse<WebRecommendList> searchWebRecommend(String title, Integer generationId, int currentPage, int pageSize) {
        log.info("웹 추천 검색 시작 - 기수: {}, 검색어: {}", generationId, title);

        int totalElements = webRecommendMapper.countSearchWebRecommend(title, generationId);
        PageInfo pageInfo = new PageInfo(currentPage, pageSize, totalElements);
        int offset = (currentPage-1)*pageSize;

        List<WebRecommendList> webRecommends = webRecommendMapper.searchWebRecommendByTitle(title, generationId, offset, pageSize);

        log.info("웹 추천 검색 완료 - 검색된 사이트 수: {}", webRecommends.size());
        return new PageResponse<>(webRecommends, pageInfo);
    }

    @Override
    public WebRecommendDetail getWebRecommend(int recommendId) {
        log.info("웹 추천 상세 조회 시작 - ID: {}", recommendId);

        WebRecommendDetail webRecommendDetail = webRecommendMapper.selectWebRecommendById(recommendId);

        if (webRecommendDetail == null) {
            log.info("웹 추천 상세 조회 실패 - 데이터가 존재하지 않습니다. ID: {}", recommendId);
            throw new BusinessException(ErrorCode.BOARD_NOT_FOUND);
        }

        log.info("웹 추천 상세 조회 완료 - ID: {}", recommendId);
        return webRecommendDetail;
    }


    @Override
    public CreateResponse addWebRecommend(WebRecommendRequest webRecommend, int userId) {
        log.info("웹 추천 등록 시작 - 제목: {}", webRecommend.getTitle());

        // URL 중복 확인
        int count = webRecommendMapper.existByUrl(webRecommend.getUrl());
        if (count > 0) {
            log.warn("이미 존재하는 URL - {}", webRecommend.getUrl());
            throw new BusinessException(DUPLICATE_URL);
        }

        CreateResponse response = new CreateResponse();
        int result = webRecommendMapper.insertWebRecommend(webRecommend, userId, response);

        if (result <= 0) {
            log.error("웹 추천 등록 실패 - 제목: {}", webRecommend.getTitle());
            throw new IllegalArgumentException("웹 추천 등록에 실패했습니다.");
        }

        log.info("웹 추천 등록 완료 - ID: {}", response.getId());

        return response;
    }

    @Override
    public void updateWebRecommend(int recommendId, WebRecommendRequest webRecommend, int userId) {
        log.info("웹 추천 수정 시작 - ID: {}", recommendId);

        int result = webRecommendMapper.updateWebRecommend(recommendId, webRecommend, userId);

        if (result <= 0) {
            log.error("웹 추천 수정 실패 - ID: {}", recommendId);
            throw new IllegalArgumentException("웹 추천 수정에 실패했습니다.");
        }

        log.info("웹 추천 수정 완료 - ID: {}", recommendId);
    }

    @Override
    public void deleteWebRecommend(int recommendId) {
        log.info("웹 추천 삭제 시작 - ID: {}", recommendId);

        int result = webRecommendMapper.deleteWebRecommend(recommendId);

        if (result <= 0) {
            log.error("웹 추천 삭제 실패 - ID: {}", recommendId);
            throw new IllegalArgumentException("웹 추천 삭제에 실패했습니다.");
        }

        log.info("웹 추천 삭제 완료 - ID: {}", recommendId);
    }
}