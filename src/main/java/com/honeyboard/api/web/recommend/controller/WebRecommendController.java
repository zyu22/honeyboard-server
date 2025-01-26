package com.honeyboard.api.web.recommend.controller;

import com.honeyboard.api.common.model.CreateResponse;
import com.honeyboard.api.common.response.PageResponse;
import com.honeyboard.api.user.model.CurrentUser;
import com.honeyboard.api.user.model.User;
import com.honeyboard.api.web.recommend.model.request.WebRecommendRequest;
import com.honeyboard.api.web.recommend.model.response.WebRecommendDetail;
import com.honeyboard.api.web.recommend.model.response.WebRecommendList;
import com.honeyboard.api.web.recommend.service.WebRecommendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.C;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/web/recommend")
@RequiredArgsConstructor
@Slf4j
public class WebRecommendController {
    private final WebRecommendService webRecommendService;

    @GetMapping
    public ResponseEntity<?> getAllWebRecommend(
            @RequestParam(value = "generationId", defaultValue = "0") int generationId,
            @RequestParam(defaultValue = "1") int currentPage,
            @RequestParam(defaultValue = "16") int pageSize,
            @RequestParam(required = false) String title) {

        log.info("웹 추천 조회 요청 - 기수: {}, 페이지: {}, 사이즈: {}, 검색어: {}",
                generationId, currentPage, pageSize, title);

        PageResponse<WebRecommendList> pageResponse;

        if (title != null) {
            pageResponse = webRecommendService.searchWebRecommend(title, generationId, currentPage, pageSize);
            log.info("웹 추천 검색 완료 - 검색어: {}", title);
        } else {
            pageResponse = webRecommendService.getAllWebRecommend(
                    generationId == 0 ? null : generationId,
                    currentPage,
                    pageSize
            );
            log.info("웹 추천 전체 조회 완료");
        }

        if (pageResponse.getContent().isEmpty()) {
            log.info("조회 결과 - 데이터 없음");
            return ResponseEntity.noContent().build();
        }

        log.info("조회 결과 - 개수: {}", pageResponse.getContent().size());
        return ResponseEntity.ok(pageResponse);
    }

    @GetMapping("/{recommendId}")
    public ResponseEntity<?> getWebRecommend(@PathVariable int recommendId,
                                             @CurrentUser User user) {
        log.info("웹 추천 상세 조회 요청 - ID: {}", recommendId);
        WebRecommendDetail webRecommend = webRecommendService.getWebRecommend(recommendId, user.getUserId());
        log.info("웹 추천 상세 조회 완료 - ID: {}", recommendId);
        return ResponseEntity.ok(webRecommend);
    }

    @PostMapping
    public ResponseEntity<?> addWebRecommend(@RequestBody WebRecommendRequest webRecommend,
                                             @CurrentUser User user) {
        // 사용자 ID가 null인 경우 처리
        if (user == null || user.getUserId() == 0) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        log.info("웹 추천 작성 요청 ID: {}", user.getUserId());

        CreateResponse createResponse = webRecommendService.addWebRecommend(webRecommend, user.getUserId(), user.getGenerationId());
        log.info("웹 추천 작성 완료");
        return ResponseEntity.status(HttpStatus.CREATED).body(createResponse);
    }

    @PutMapping("/{recommendId}")
    public ResponseEntity<?> updateWebRecommend(
            @PathVariable int recommendId,
            @RequestBody WebRecommendRequest webRecommend,
            @CurrentUser User user) {
        log.info("웹 추천 수정 요청 - ID: {}", recommendId);
        webRecommendService.updateWebRecommend(recommendId, webRecommend, user.getUserId());
        log.info("웹 추천 수정 완료 - ID: {}", recommendId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{recommendId}")
    public ResponseEntity<?> deleteWebRecommend(@PathVariable int recommendId) {
        log.info("웹 추천 삭제 요청 - ID: {}", recommendId);
        webRecommendService.deleteWebRecommend(recommendId);
        log.info("웹 추천 삭제 완료 - ID: {}", recommendId);
        return ResponseEntity.ok().build();
    }
}