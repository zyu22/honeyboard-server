package com.honeyboard.api.algorithm.guide.controller;

import com.honeyboard.api.algorithm.guide.model.request.AlgorithmGuideRequest;
import com.honeyboard.api.algorithm.guide.model.response.AlgorithmGuideDetail;
import com.honeyboard.api.algorithm.guide.model.response.AlgorithmGuideList;
import com.honeyboard.api.algorithm.guide.service.AlgorithmGuideService;
import com.honeyboard.api.common.model.CreateResponse;
import com.honeyboard.api.common.response.PageResponse;
import com.honeyboard.api.user.model.CurrentUser;
import com.honeyboard.api.user.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/algorithm/guide")
@RequiredArgsConstructor
@Slf4j
public class AlgorithmGuideController {

    private final AlgorithmGuideService algorithmGuideService;

    // 알고리즘 개념 전체 조회 및 검색
    @GetMapping
    public ResponseEntity<?> getAllAlgorithmGuide(
    		@RequestParam(defaultValue = "1") int currentPage,
            @RequestParam(defaultValue = "8") int pageSize,
            @RequestParam(defaultValue = "0") int generationId,
            @RequestParam(required = false) String title) {

        log.info("알고리즘 개념 전체 조회 요청 - 기수: {}, 검색어: {}", generationId, title);
        PageResponse<AlgorithmGuideList> algorithmGuideList;
        if(title != null) {
            algorithmGuideList = algorithmGuideService.searchAlgorithmGuide(currentPage, pageSize, generationId, title);
        }else {
            algorithmGuideList = algorithmGuideService.getAlgorithmGuides(currentPage, pageSize, generationId);
        }

        if (algorithmGuideList == null) {
            log.info("알고리즘 개념 전체 조회 완료 - 데이터 없음");
            return ResponseEntity.noContent().build();
        }

        log.info("알고리즘 개념 전체 조회 완료 - 조회된 개수: {}", algorithmGuideList.getContent().size());
        return ResponseEntity.ok(algorithmGuideList);
    }

    // 알고리즘 개념 설명 상세 조회
    @GetMapping("/{guideId}")
    public ResponseEntity<AlgorithmGuideDetail> getAlgorithmGuideDetail(@PathVariable("guideId") int id,
                                                                        @CurrentUser User user) {
        log.info("알고리즘 개념 상세 조회 요청 - ID: {}. 유저 ID: {}", id, user.getUserId());
        AlgorithmGuideDetail algorithmGuideDetail = algorithmGuideService.getAlgorithmGuideDetail(id, user.getUserId());
        
        if (algorithmGuideDetail == null) {
            log.info("알고리즘 개념 상세 조회 완료 - 데이터 없음");
            return ResponseEntity.noContent().build();
        }

        log.info("알고리즘 개념 상세 조회 완료 - ID: {}", id);
        return ResponseEntity.ok(algorithmGuideDetail);
    }

    // 알고리즘 개념 설명 작성
    @PostMapping

    public ResponseEntity<CreateResponse> addAlgorithmGuide(
            @RequestBody AlgorithmGuideRequest algorithmGuideRequest,
            @CurrentUser User user) {

        log.info("알고리즘 개념 작성 요청 - 제목 : {}", algorithmGuideRequest.getTitle());
        CreateResponse createResponse = algorithmGuideService.addAlgorithmGuide(algorithmGuideRequest, user.getUserId());
        log.info("알고리즘 개념 작성 완료 - ID: {}", createResponse.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(createResponse);
    }

    @PutMapping("/{guideId}")
    public ResponseEntity<Void> updateAlgorithmGuide(
            @PathVariable("guideId") int id,
            @RequestBody AlgorithmGuideRequest algorithmGuideRequest,
            @CurrentUser User user) {

        log.info("알고리즘 개념 수정 요청 - ID: {}", id);
        algorithmGuideService.updateAlgorithmGuide(id, algorithmGuideRequest, user.getUserId());
        log.info("알고리즘 개념 수정 완료 - ID: {}", id);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{guideId}")
    public ResponseEntity<Void> softDeleteAlgorithmGuide(@PathVariable("guideId") int id) {
        log.info("알고리즘 개념 삭제 요청 - ID: {}", id);
        algorithmGuideService.softDeleteAlgorithmGuide(id);
        log.info("알고리즘 개념 삭제 완료 - ID: {}", id);
        return ResponseEntity.ok().build();
    }

}