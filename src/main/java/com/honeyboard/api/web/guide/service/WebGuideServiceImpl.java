package com.honeyboard.api.web.guide.service;

import com.honeyboard.api.common.model.PageInfo;
import com.honeyboard.api.common.response.PageResponse;
import com.honeyboard.api.web.guide.mapper.WebGuideMapper;
import com.honeyboard.api.web.guide.model.WebGuide;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebGuideServiceImpl implements WebGuideService {
    private final WebGuideMapper webGuideMapper;

    @Override
    public PageResponse<WebGuide> getAllWebGuide(Integer generationId, int currentPage, int pageSize) {
        log.info("웹 개념 전체 조회 시작 - 기수: {}", generationId);

        int totalElements = webGuideMapper.countWebGuide(generationId);
        PageInfo pageInfo = new PageInfo(currentPage, pageSize, totalElements);
        int offset = (currentPage-1) * pageSize;

        List<WebGuide> webGuides = webGuideMapper.selectAllWebGuide(generationId, offset);

        log.info("웹 개념 전체 조회 완료 - 조회된 개념 수: {}", webGuides.size());
        return new PageResponse<>(webGuides, pageInfo);
    }

    @Override
    public PageResponse<WebGuide> searchWebGuide(String title, Integer generationId, int currentPage, int pageSize) {
        log.info("웹 개념 검색 시작 - 기수: {}, 검색어: {}", generationId, title);

        int totalElements = webGuideMapper.countSearchWebGuide(title, generationId);
        PageInfo pageInfo = new PageInfo(currentPage, pageSize, totalElements);
        int offset = (currentPage-1) * pageSize;

        List<WebGuide> webGuides = webGuideMapper.searchWebGuideByTitle(title, generationId, offset);

        log.info("웹 개념 검색 완료 - 검색된 개념 수: {}", webGuides.size());
        return new PageResponse<>(webGuides, pageInfo);
    }

    @Override
    public WebGuide getWebGuide(int guideId) {
        log.info("웹 개념 상세 조회 시작 - ID: {}", guideId);

        WebGuide webGuide = webGuideMapper.selectWebGuideById(guideId);

        if (webGuide == null) {
            log.error("웹 개념 상세 조회 실패 - 데이터가 존재하지 않습니다. ID: {}", guideId);
            throw new IllegalArgumentException("웹 개념 정보가 존재하지 않습니다.");
        }

        log.info("웹 개념 상세 조회 완료 - ID: {}", guideId);
        return webGuide;
    }

    @Override
    public void addWebGuide(WebGuide webGuide) {
        log.info("웹 개념 등록 시작");

        int result = webGuideMapper.insertWebGuide(webGuide);

        if (result != 1) {
            log.error("웹 개념 등록 실패");
            throw new IllegalArgumentException("웹 개념 등록에 실패했습니다.");
        }

        log.info("웹 개념 등록 완료 - ID: {}", webGuide.getId());
    }

    @Override
    public void updateWebGuide(int guideId, WebGuide webGuide) {
        log.info("웹 개념 수정 시작 - ID: {}", webGuide.getId());

        int result = webGuideMapper.updateWebGuide(guideId, webGuide);

        if (result != 1) {
            log.error("웹 개념 수정 실패 - ID: {}", guideId);
            throw new IllegalArgumentException("웹 개념 수정에 실패했습니다.");
        }

        log.info("웹 개념 수정 완료 - ID: {}", webGuide.getId());
    }

    @Override
    public void deleteWebGuide(int guideId) {
        log.info("웹 개념 삭제 시작 - ID: {}", guideId);

        int result = webGuideMapper.deleteWebGuide(guideId);

        if (result != 1) {
            log.error("웹 개념 삭제 실패 - ID: {}", guideId);
            throw new IllegalArgumentException("웹 개념 삭제에 실패했습니다.");
        }

        log.info("웹 개념 삭제 완료 - ID: {}", guideId);
    }
}