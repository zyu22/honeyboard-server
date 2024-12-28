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

    // 웹 학습사이트 전체 조회 GET /api/v1/web/recommend?generation={generationId}
    @GetMapping
    public ResponseEntity<?> getAllWebRecommend(
            @RequestParam Integer generationId,
            @RequestParam(defaultValue = "1") int page) {
        try {
            PageResponse<WebRecommend> pageResponse = webRecommendService.getAllWebRecommend(generationId, page);

            if (pageResponse.getContent().isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok().body(pageResponse);
        } catch (Exception e) {
            log.error("웹 학습사이트 전체 조회 에러: ", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 웹 학습사이트 검색 GET /api/v1/web/recommend?generation={generationId}&title={title}
    @GetMapping("/search")
    public ResponseEntity<?> searchWebRecommend(
            @RequestParam Integer generationId,
            @RequestParam String title,
            @RequestParam(defaultValue = "1") int page) {
        try {
            PageResponse<WebRecommend> pageResponse = webRecommendService.searchWebRecommend(title, generationId, page);

            if (pageResponse.getContent().isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok().body(pageResponse);
        } catch (Exception e) {
            log.error("웹 학습사이트 검색 조회 에러: ", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 웹 학습사이트 상세 조회 GET /api/v1/web/recommend/{recommendId}
    @GetMapping("/{recommendId}")
    public ResponseEntity<?> getWebRecommend(@PathVariable int recommendId) {
        try {
            WebRecommend webRecommend = webRecommendService.getWebRecommend(recommendId);
            return ResponseEntity.ok().body(webRecommend);
        } catch (Exception e) {
            log.error("웹 학습사이트 상세 조회 에러: ", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> addWebRecommend(@RequestBody WebRecommend webRecommend) {
        try {
            boolean result = webRecommendService.addWebRecommend(webRecommend);
            
            if (result) {
                return ResponseEntity.status(HttpStatus.CREATED).body("웹 학습사이트가 작성되었습니다.");
            }
            throw new Exception("웹 학습사이트 작성에 실패했습니다.");
        } catch (Exception e) {
            log.error("웹 학습사이트 작성 에러: ", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{recommendId}")
    public ResponseEntity<?> updateWebRecommend(
            @PathVariable int recommendId,
            @RequestBody WebRecommend webRecommend) {
        try {
            boolean result = webRecommendService.updateWebRecommend(recommendId, webRecommend);

            if (result) {
                return ResponseEntity.ok().body("웹 학습사이트가 수정되었습니다.");
            }
            throw new Exception("웹 학습사이트 수정에 실패했습니다.");
        } catch (Exception e) {
            log.error("웹 학습사이트 수정 에러: ", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{recommendId}")
    public ResponseEntity<?> deleteWebRecommend(@PathVariable int recommendId) {
        try {
            boolean result = webRecommendService.deleteWebRecommend(recommendId);

            if (result) {
                return ResponseEntity.ok().body("웹 학습사이트가 삭제되었습니다.");
            }
            throw new Exception("웹 학습사이트 삭제에 실패했습니다.");
        } catch (Exception e) {
            log.error("웹 학습사이트 삭제 에러: ", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
