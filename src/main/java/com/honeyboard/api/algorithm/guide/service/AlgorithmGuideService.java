package com.honeyboard.api.algorithm.guide.service;

import com.honeyboard.api.algorithm.guide.model.request.AlgorithmGuideRequest;
import com.honeyboard.api.algorithm.guide.model.response.AlgorithmGuideDetail;
import com.honeyboard.api.algorithm.guide.model.response.AlgorithmGuideList;
import com.honeyboard.api.common.model.CreateResponse;
import com.honeyboard.api.common.response.PageResponse;
import org.apache.ibatis.annotations.Param;

public interface AlgorithmGuideService {

    // 알고리즘 개념 전체 조회 (검색어 X)
	PageResponse<AlgorithmGuideList> getAlgorithmGuides(int currentPage, int pageSize, int generationId);

    // 알고리즘 개념 검색 구현
    PageResponse<AlgorithmGuideList> searchAlgorithmGuide(int currentPage, int pageSize, int generationId, String title);

    // 알고리즘 개념 상세 조회 구현
    AlgorithmGuideDetail getAlgorithmGuideDetail (@Param("id") int id, @Param("userId") int userId);

    // 알고리즘 개념 작성
    CreateResponse addAlgorithmGuide(AlgorithmGuideRequest algorithmGuideRequest, int userId);

    //알고리즘 개념 수정 (PUT /api/v1/algorithm/guide/{id}) 구현
    void updateAlgorithmGuide(int guideId, AlgorithmGuideRequest algorithmGuideRequest, int userId);

    //알고리즘 개념 삭제 (DELETE /api/v1/algorithm/guide/{id}) 구현
    void softDeleteAlgorithmGuide(int id);
}
