package com.honeyboard.api.web.recommend.controller;

import com.honeyboard.api.common.response.PageResponse;
import com.honeyboard.api.web.recommend.model.WebRecommend;
import com.honeyboard.api.web.recommend.service.WebRecommendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
            @RequestParam Integer generationId,
            @RequestParam(defaultValue = "1") int currentPage,
            @RequestParam(defaultValue = "16") int pageSize) {
        log.info("웹 학습사이트 전체 조회 요청 - 기수: {}, 페이지: {}, 사이즈: {}", generationId, currentPage, pageSize);
        PageResponse<WebRecommend> pageResponse = webRecommendService.getAllWebRecommend(generationId, currentPage, pageSize);

        if (pageResponse.getContent().isEmpty()) {
            log.info("웹 학습사이트 전체 조회 완료 - 데이터 없음");
            return ResponseEntity.noContent().build();
        }

        log.info("웹 학습사이트 전체 조회 완료 - 조회된 개수: {}", pageResponse.getContent().size());
        return ResponseEntity.ok(pageResponse);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchWebRecommend(
            @RequestParam Integer generationId,
            @RequestParam String title,
            @RequestParam(defaultValue = "1") int currentPage,
            @RequestParam(defaultValue = "16") int pageSize) {
        log.info("웹 학습사이트 검색 요청 - 기수: {}, 검색어: {}", generationId, title);
        PageResponse<WebRecommend> pageResponse = webRecommendService.searchWebRecommend(title, generationId, currentPage, pageSize);

        if (pageResponse.getContent().isEmpty()) {
            log.info("웹 학습사이트 검색 완료 - 검색 결과 없음");
            return ResponseEntity.noContent().build();
        }

        log.info("웹 학습사이트 검색 완료 - 검색된 개수: {}", pageResponse.getContent().size());
        return ResponseEntity.ok(pageResponse);
    }

    @GetMapping("/{recommendId}")
    public ResponseEntity<?> getWebRecommend(@PathVariable int recommendId) {
        log.info("웹 학습사이트 상세 조회 요청 - ID: {}", recommendId);
        WebRecommend webRecommend = webRecommendService.getWebRecommend(recommendId);
        log.info("웹 학습사이트 상세 조회 완료 - ID: {}", recommendId);
        return ResponseEntity.ok(webRecommend);
    }

    @PostMapping
    public ResponseEntity<?> addWebRecommend(@RequestBody WebRecommend webRecommend) {
        log.info("웹 학습사이트 작성 요청");
        webRecommendService.addWebRecommend(webRecommend);
        log.info("웹 학습사이트 작성 완료 - ID: {}", webRecommend.getId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{recommendId}")
    public ResponseEntity<?> updateWebRecommend(
            @PathVariable int recommendId,
            @RequestBody WebRecommend webRecommend) {
        log.info("웹 학습사이트 수정 요청 - ID: {}", recommendId);
        webRecommend.setId(recommendId);
        webRecommendService.updateWebRecommend(webRecommend);
        log.info("웹 학습사이트 수정 완료 - ID: {}", recommendId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{recommendId}")
    public ResponseEntity<?> deleteWebRecommend(@PathVariable int recommendId) {
        log.info("웹 학습사이트 삭제 요청 - ID: {}", recommendId);
        webRecommendService.deleteWebRecommend(recommendId);
        log.info("웹 학습사이트 삭제 완료 - ID: {}", recommendId);
        return ResponseEntity.ok().build();
    }
}