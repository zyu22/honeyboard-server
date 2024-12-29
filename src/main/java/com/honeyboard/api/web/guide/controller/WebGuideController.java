package com.honeyboard.api.web.guide.controller;

import com.honeyboard.api.common.response.PageResponse;
import com.honeyboard.api.web.guide.model.WebGuide;
import com.honeyboard.api.web.guide.service.WebGuideService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/web/guide")
@RequiredArgsConstructor
@Slf4j
public class WebGuideController {
    private  final WebGuideService webGuideService;

    // 웹 개념 전체 조회 GET /api/v1/web/guide?generation={generationId}
    @GetMapping
    public ResponseEntity<?> getAllWebGuide(
            @RequestParam Integer generationId,
            @RequestParam(defaultValue = "1") int page) {
        try {
            PageResponse<WebGuide> pageResponse = webGuideService.getAllWebGuide(generationId, page);

            if (pageResponse.getContent().isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok().body(pageResponse);
        } catch (Exception e) {
            log.error("웹 개념 전체 조회 에러: ", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 웹 개념 검색 GET /api/v1/web/guide?generation={generationId}&title={title}
    @GetMapping("/search")
    public ResponseEntity<?> searchWebGuide(
            @RequestParam Integer generationId,
            @RequestParam String title,
            @RequestParam(defaultValue = "1") int page) {
        try {
            PageResponse<WebGuide> pageResponse = webGuideService.searchWebGuide(title, generationId, page);

            if (pageResponse.getContent().isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok().body(pageResponse);
        } catch (Exception e) {
            log.error("웹 개념 검색 조회 에러: ", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 웹 개념 상세 조회 GET /api/v1/web/guide/{guideId}
    @GetMapping("/{guideId}")
    public ResponseEntity<?> getWebGuide(@PathVariable int guideId) {
        try {
            WebGuide webGuide = webGuideService.getWebGuide(guideId);
            return ResponseEntity.ok().body(webGuide);
        } catch (Exception e) {
            log.error("웹 개념 상세 조회 에러: ", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 웹 개념 작성 POST /api/v1/web/guide
    @PostMapping
    public ResponseEntity<?> addWebGuide(@RequestBody WebGuide webGuide) {
        try {
            boolean result = webGuideService.addWebGuide(webGuide);

            if (result) {
                return ResponseEntity.status(HttpStatus.CREATED).body("웹 개념이 작성되었습니다.");
            }

            throw new Exception("웹 개념 작성에 실패했습니다.");
        } catch (Exception e) {
            log.error("웹 개념 작성 에러: ", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 웹 개념 수정 PUT /api/v1/web/guide/{guideId}}
    @PutMapping("/{guideId}")
    public ResponseEntity<?> updateWebGuide(
            @PathVariable int guideId,
            @RequestBody WebGuide webGuide) {
        try {
            boolean result = webGuideService.updateWebGuide(guideId, webGuide);

            if (result) {
                return ResponseEntity.ok().body("웹 개념이 수정되었습니다.");
            }

            throw new Exception("웹 개념 수정에 실패했습니다.");
        } catch (Exception e) {
            log.error("웹 개념 수정 에러: ", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 웹 개념 삭제 DELETE /api/v1/web/guide/{guideId}
    @DeleteMapping("/{guideId}")
    public ResponseEntity<?> deleteWebGuide(@PathVariable int guideId) {
        try {
            boolean result = webGuideService.deleteWebGuide(guideId);

            if (result) {
                return ResponseEntity.ok().body("웹 개념이 삭제되었습니다.");
            }

            throw new Exception("웹 개념 삭제에 실패했습니다.");
        } catch (Exception e) {
            log.error("웹 개념 삭제 에러: ", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}