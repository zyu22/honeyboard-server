package com.honeyboard.api.web.guide.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.honeyboard.api.common.model.CreateResponse;
import com.honeyboard.api.common.response.PageResponse;
import com.honeyboard.api.user.model.CurrentUser;
import com.honeyboard.api.user.model.User;
import com.honeyboard.api.web.guide.model.request.WebGuideRequest;
import com.honeyboard.api.web.guide.model.response.WebGuideDetail;
import com.honeyboard.api.web.guide.model.response.WebGuideList;
import com.honeyboard.api.web.guide.service.WebGuideService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/web/guide")
@RequiredArgsConstructor
@Slf4j
public class WebGuideController {
    private final WebGuideService webGuideService;

    @GetMapping
    public ResponseEntity<?> getAllWebGuide(
            @RequestParam(value = "generationId", defaultValue = "0") int generationId,
            @RequestParam(defaultValue = "1") int currentPage,
            @RequestParam(defaultValue = "8") int pageSize, 
            @RequestParam(required = false) String title) {
        
    	log.info("웹 개념 전체 조회 요청 - 기수: {}, 페이지: {}, 사이즈: {}, 검색: {}", generationId, currentPage, pageSize, title);
        PageResponse<WebGuideList> response;
        if(title != null) response = webGuideService.searchWebGuide(generationId, currentPage, pageSize, title);
        else response = webGuideService.getAllWebGuide(generationId, currentPage, pageSize);
        
        if (response==null) {
            log.info("웹 개념 전체 조회 완료 - 데이터 없음");
            return ResponseEntity.noContent().build();
        }

        log.info("웹 개념 전체 조회 완료 - 조회된 개수: {}", response.getContent().size());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{guideId}")
    public ResponseEntity<?> getWebGuideDetail(@PathVariable("guideId") int guideId,
    											@CurrentUser User user) {
        log.info("웹 개념 상세 조회 요청 - ID: {}", guideId);
        WebGuideDetail webGuideDetail = webGuideService.getWebGuideDetail(guideId, user.getUserId());
        log.info("웹 개념 상세 조회 완료 - ID: {}", guideId);
        return ResponseEntity.ok(webGuideDetail);
    }

    @PostMapping
    public ResponseEntity<?> createWebGuide(@RequestBody WebGuideRequest webGuideRequest) {
        log.info("웹 개념 작성 요청 - 제목:{}", webGuideRequest.getTitle());
        CreateResponse createResponse = webGuideService.createWebGuide(webGuideRequest);
        log.info("웹 개념 작성 완료 - ID: {}", createResponse.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(createResponse.getId());
    }

    @PutMapping("/{guideId}")
    public ResponseEntity<?> updateWebGuide(
            @PathVariable int guideId,
            @RequestBody WebGuideRequest webGuideRequest) {
        log.info("웹 개념 수정 요청 - ID: {}", guideId);
        webGuideService.updateWebGuide(guideId, webGuideRequest);
        log.info("웹 개념 수정 완료 - ID: {}", guideId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{guideId}")
    public ResponseEntity<?> softDeleteWebGuide(@PathVariable("guideId") int guideId) {
        log.info("웹 개념 삭제 요청 - ID: {}", guideId);
        webGuideService.softDeleteWebGuide(guideId);
        log.info("웹 개념 삭제 완료 - ID: {}", guideId);
        return ResponseEntity.ok().build();
    }
}