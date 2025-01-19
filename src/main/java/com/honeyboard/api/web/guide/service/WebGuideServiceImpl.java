package com.honeyboard.api.web.guide.service;

import java.util.List;

import com.honeyboard.api.exception.BusinessException;
import com.honeyboard.api.exception.ErrorCode;
import org.springframework.stereotype.Service;

import com.honeyboard.api.common.model.CreateResponse;
import com.honeyboard.api.common.model.PageInfo;
import com.honeyboard.api.common.response.PageResponse;
import com.honeyboard.api.web.guide.mapper.WebGuideMapper;
import com.honeyboard.api.web.guide.model.request.WebGuideRequest;
import com.honeyboard.api.web.guide.model.response.WebGuideDetail;
import com.honeyboard.api.web.guide.model.response.WebGuideList;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebGuideServiceImpl implements WebGuideService {
    private final WebGuideMapper webGuideMapper;

    @Override
    public PageResponse<WebGuideList> getAllWebGuide(int generationId, int currentPage, int pageSize) {
        log.info("웹 개념 전체 조회 시작 - 기수: {}", generationId);

        int totalElements = webGuideMapper.countWebGuide(generationId);
        PageInfo pageInfo = new PageInfo(currentPage, pageSize, totalElements);
        int offset = (currentPage-1) * pageSize;

        List<WebGuideList> webGuideList = webGuideMapper.selectAllWebGuide(generationId, offset, pageSize);

        log.info("웹 개념 전체 조회 완료 - 조회된 개념 수: {}", webGuideList.size());
        return new PageResponse<>(webGuideList, pageInfo);
    }

    @Override
    public PageResponse<WebGuideList> searchWebGuide(int generationId, int currentPage, int pageSize, String title) {
        log.info("웹 개념 검색 시작 - 기수: {}, 검색어: {}", generationId, title);

        int totalElements = webGuideMapper.countSearchWebGuide(title, generationId);
        PageInfo pageInfo = new PageInfo(currentPage, pageSize, totalElements);
        int offset = (currentPage-1) * pageSize;

        List<WebGuideList> webGuideList = webGuideMapper.searchWebGuideByTitle(generationId, offset, pageSize, title);

        log.info("웹 개념 검색 완료 - 검색된 개념 수: {}", webGuideList.size());
        return new PageResponse<>(webGuideList, pageInfo);
    }

    @Override
    public WebGuideDetail getWebGuideDetail(int guideId, int userId) {
        log.info("웹 개념 상세 조회 시작 - ID: {}", guideId);

        WebGuideDetail webGuideDetail = webGuideMapper.selectWebGuideById(guideId, userId);

        if (webGuideDetail == null) {
            log.info("웹 개념 상세 조회 실패 - 데이터가 존재하지 않습니다. ID: {}", guideId);
            throw new IllegalArgumentException("웹 개념 정보가 존재하지 않습니다.");
        }

        log.info("웹 개념 상세 조회 완료 - ID: {}", guideId);
        return webGuideDetail;
    }

    @Override
    public CreateResponse createWebGuide(WebGuideRequest webGuideRequest, int userId, int generationId) {
        log.info("웹 개념 등록 시작 - 제목: {}", webGuideRequest.getTitle());
        
        CreateResponse createResponse = new CreateResponse();
        int insertresult = webGuideMapper.createWebGuide(webGuideRequest, userId, generationId, createResponse);

        if (insertresult <= 0) {
            log.info("웹 개념 등록 실패 - 제목: {}", webGuideRequest.getTitle());
            throw new BusinessException(ErrorCode.BOARD_CREATE_FAILED);
        }
        createResponse.setId(insertresult);
        log.info("웹 개념 등록 완료 - ID: {}", createResponse.getId());
        return createResponse;
    }

    @Override
    public void updateWebGuide(int guideId, WebGuideRequest webGuideRequest, int userId) {
        log.info("웹 개념 수정 시작 - ID: {}", guideId);

        int result = webGuideMapper.updateWebGuide(guideId, webGuideRequest, userId);

        if (result != 1) {
            log.info("웹 개념 수정 실패 - ID: {}", guideId);
            throw new BusinessException(ErrorCode.BOARD_UPDATE_FAILED);
        }

        log.info("웹 개념 수정 완료 - ID: {}", guideId);
    }

    @Override
    public void softDeleteWebGuide(int guideId, int userId) {
        log.info("웹 개념 삭제 시작 - ID: {}", guideId);

        int result = webGuideMapper.softDeleteWebGuide(guideId, userId);

        if (result != 1) {
            log.info("웹 개념 삭제 실패 - ID: {}", guideId);
            throw new BusinessException(ErrorCode.BOARD_DELETE_FAILED);
        }

        log.info("웹 개념 삭제 완료 - ID: {}", guideId);
    }
}