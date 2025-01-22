package com.honeyboard.api.algorithm.guide.service;

import com.honeyboard.api.algorithm.guide.mapper.AlgorithmGuideMapper;
import com.honeyboard.api.algorithm.guide.model.request.AlgorithmGuideRequest;
import com.honeyboard.api.algorithm.guide.model.response.AlgorithmGuideDetail;
import com.honeyboard.api.algorithm.guide.model.response.AlgorithmGuideList;
import com.honeyboard.api.common.model.CreateResponse;
import com.honeyboard.api.common.model.PageInfo;
import com.honeyboard.api.common.response.PageResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlgorithmGuideServiceImpl implements AlgorithmGuideService {

    private final AlgorithmGuideMapper algorithmGuideMapper;

    // 알고리즘 개념 전체 리스트 (검색어 X) 조회
    @Override
    public PageResponse<AlgorithmGuideList> getAlgorithmGuides(int currentPage, int pageSize, int generationId) {
    	int totalElement = algorithmGuideMapper.countAlgorithmGuide(generationId);
        int offset = (currentPage - 1) * pageSize;

        PageInfo pageInfo = new PageInfo(currentPage, pageSize, totalElement);

    	List<AlgorithmGuideList> list = algorithmGuideMapper.getAlgorithmGuides(offset, pageSize, generationId);

        return new PageResponse<>(list, pageInfo);
    }

    // 알고리즘 개념 전체 리스트 (검색어 O) 조회
    @Override
    public PageResponse<AlgorithmGuideList> searchAlgorithmGuide(int currentPage, int pageSize, int generationId, String title) {
        int totalElement = algorithmGuideMapper.countSearchAlgorithmGuide(generationId, title);
        int offset = (currentPage - 1) * pageSize;

        PageInfo pageInfo = new PageInfo(currentPage, pageSize, totalElement);

        List<AlgorithmGuideList> list = algorithmGuideMapper.searchAlgorithmGuide(offset, pageSize, generationId, title);
        return new PageResponse<>(list, pageInfo);
    }

    // 알고리즘 개념 상세 조회
    @Override
    public AlgorithmGuideDetail getAlgorithmGuideDetail(int id, int userId) {

        if (id <= 0) {
            throw new IllegalArgumentException("유효하지 않은 가이드 ID입니다.");
        }

        log.info("id : {}, userId: {}", id, userId);
        AlgorithmGuideDetail algorithmGuideDetail = algorithmGuideMapper.getAlgorithmGuideDetail(id, userId);
        if (algorithmGuideDetail == null) {
            throw new IllegalArgumentException("해당 알고리즘 가이드를 찾을 수 없습니다.");
        }

        return algorithmGuideDetail;
    }

    // 알고리즘 개념 설명 추가
    @Override
    public CreateResponse addAlgorithmGuide(AlgorithmGuideRequest algorithmGuideRequest, int userId) {
        // 유효성 검사
        validateGuide(algorithmGuideRequest);

        CreateResponse createResponse = new CreateResponse();
        algorithmGuideMapper.addAlgorithmGuide(algorithmGuideRequest, userId, createResponse);

        if (createResponse.getId() <= 0) {
            throw new IllegalArgumentException("생성된 알고리즘 가이드 ID를 가져오는데 실패했습니다.");
        }

        return createResponse;
    }

    // 알고리즘 개념 설명 수정
    @Override
    public void updateAlgorithmGuide(int guideId, AlgorithmGuideRequest algorithmGuideRequest, int userId) {
        // 유효성 검사
        validateGuide(algorithmGuideRequest);

        int updatedRows = algorithmGuideMapper.updateAlgorithmGuide(guideId, algorithmGuideRequest, userId);
        if (updatedRows <= 0) {
            throw new IllegalArgumentException("알고리즘 가이드 수정에 실패했습니다. 해당 게시글이 존재하지 않거나 수정 권한이 없습니다.");
        }
    }

    // 알고리즘 개념 삭제
    @Override
    public void softDeleteAlgorithmGuide(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("유효하지 않은 가이드 ID입니다.");
        }

        int result = algorithmGuideMapper.softDeleteAlgorithmGuide(id);
        if (result != 1) {
            throw new IllegalArgumentException("알고리즘 가이드 삭제에 실패했습니다. 해당 게시글이 존재하지 않거나 삭제 권한이 없습니다.");
        }
    }

    // 유효성 검사
    private void validateGuide(AlgorithmGuideRequest algorithmGuideRequest) {
        if (algorithmGuideRequest == null) {
            throw new IllegalArgumentException("가이드 정보가 없습니다.");
        }
        if (algorithmGuideRequest.getTitle() == null || algorithmGuideRequest.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("제목을 입력해주세요.");
        }
        if (algorithmGuideRequest.getContent() == null || algorithmGuideRequest.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("내용을 입력해주세요.");
        }
    }

}
