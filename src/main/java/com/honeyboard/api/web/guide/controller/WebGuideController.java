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
    private final WebGuideService webGuideService;

    @GetMapping
    public ResponseEntity<?> getAllWebGuide(
            @RequestParam Integer generationId,
            @RequestParam(defaultValue = "1") int currentPage,
            @RequestParam(defaultValue = "8") int pageSize) {
        log.info("웹 개념 전체 조회 요청 - 기수: {}, 페이지: {}, 사이즈: {}", generationId, currentPage, pageSize);
        PageResponse<WebGuide> pageResponse = webGuideService.getAllWebGuide(generationId, currentPage, pageSize);

        if (pageResponse.getContent().isEmpty()) {
            log.info("웹 개념 전체 조회 완료 - 데이터 없음");
            return ResponseEntity.noContent().build();
        }

        log.info("웹 개념 전체 조회 완료 - 조회된 개수: {}", pageResponse.getContent().size());
        return ResponseEntity.ok(pageResponse);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchWebGuide(
            @RequestParam Integer generationId,
            @RequestParam String title,
            @RequestParam(defaultValue = "1") int currentPage,
            @RequestParam(defaultValue = "8") int pageSize) {
        log.info("웹 개념 검색 요청 - 기수: {}, 검색어: {}", generationId, title);
        PageResponse<WebGuide> pageResponse = webGuideService.searchWebGuide(title, generationId, currentPage, pageSize);

        if (pageResponse.getContent().isEmpty()) {
            log.info("웹 개념 검색 완료 - 검색 결과 없음");
            return ResponseEntity.noContent().build();
        }

        log.info("웹 개념 검색 완료 - 검색된 개수: {}", pageResponse.getContent().size());
        return ResponseEntity.ok(pageResponse);
    }

    @GetMapping("/{guideId}")
    public ResponseEntity<?> getWebGuide(@PathVariable int guideId) {
        log.info("웹 개념 상세 조회 요청 - ID: {}", guideId);
        WebGuide webGuide = webGuideService.getWebGuide(guideId);
        log.info("웹 개념 상세 조회 완료 - ID: {}", guideId);
        return ResponseEntity.ok(webGuide);
    }

    @PostMapping
    public ResponseEntity<?> addWebGuide(@RequestBody WebGuide webGuide) {
        log.info("웹 개념 작성 요청");
        webGuideService.addWebGuide(webGuide);
        log.info("웹 개념 작성 완료 - ID: {}", webGuide.getId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{guideId}")
    public ResponseEntity<?> updateWebGuide(
            @PathVariable int guideId,
            @RequestBody WebGuide webGuide) {
        log.info("웹 개념 수정 요청 - ID: {}", guideId);
        webGuideService.updateWebGuide(guideId, webGuide);
        log.info("웹 개념 수정 완료 - ID: {}", guideId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{guideId}")
    public ResponseEntity<?> deleteWebGuide(@PathVariable int guideId) {
        log.info("웹 개념 삭제 요청 - ID: {}", guideId);
        webGuideService.deleteWebGuide(guideId);
        log.info("웹 개념 삭제 완료 - ID: {}", guideId);
        return ResponseEntity.ok().build();
    }
}