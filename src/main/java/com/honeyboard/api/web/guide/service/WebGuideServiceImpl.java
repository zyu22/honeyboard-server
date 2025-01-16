package com.honeyboard.api.web.guide.service;

import java.util.List;

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
            log.error("웹 개념 상세 조회 실패 - 데이터가 존재하지 않습니다. ID: {}", guideId);
            throw new IllegalArgumentException("웹 개념 정보가 존재하지 않습니다.");
        }

        log.info("웹 개념 상세 조회 완료 - ID: {}", guideId);
        return webGuideDetail;
    }

    @Override
    public CreateResponse createWebGuide(WebGuideRequest webGuideRequest) {
        log.info("웹 개념 등록 시작 - 제목: {}", webGuideRequest.getTitle());
        
        CreateResponse createResponse = new CreateResponse();
        webGuideMapper.createWebGuide(webGuideRequest, createResponse);

        if (createResponse.getId() <= 0) {
            log.error("웹 개념 등록 실패 - 제목: {}", webGuideRequest.getTitle());
            throw new IllegalArgumentException("생성된 웹 개념 ID를 가져오는데 실패했습니다.");
        }

        log.info("웹 개념 등록 완료 - ID: {}", createResponse.getId());
        return createResponse;
    }

    @Override
    public void updateWebGuide(int guideId, WebGuideRequest webGuideRequest) {
        log.info("웹 개념 수정 시작 - ID: {}", guideId);

        int result = webGuideMapper.updateWebGuide(guideId, webGuideRequest);

        if (result != 1) {
            log.error("웹 개념 수정 실패 - ID: {}", guideId);
            throw new IllegalArgumentException("웹 개념 수정에 실패했습니다.");
        }

        log.info("웹 개념 수정 완료 - ID: {}", guideId);
    }

    @Override
    public void softDeleteWebGuide(int guideId) {
        log.info("웹 개념 삭제 시작 - ID: {}", guideId);

        int result = webGuideMapper.softDeleteWebGuide(guideId);

        if (result != 1) {
            log.error("웹 개념 삭제 실패 - ID: {}", guideId);
            throw new IllegalArgumentException("웹 개념 삭제에 실패했습니다.");
        }

        log.info("웹 개념 삭제 완료 - ID: {}", guideId);
    }
    
 // 유효성 검사
    private void validateWeb(WebGuideRequest webGuideRequest) {
        if (webGuideRequest == null) {
            throw new IllegalArgumentException("가이드 정보가 없습니다.");
        }
        if (webGuideRequest.getTitle() == null || webGuideRequest.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("제목을 입력해주세요.");
        }
        if (webGuideRequest.getContent() == null || webGuideRequest.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("내용을 입력해주세요.");
        }
    }
}