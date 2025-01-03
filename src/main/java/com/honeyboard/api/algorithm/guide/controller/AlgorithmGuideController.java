package com.honeyboard.api.algorithm.guide.controller;

import com.honeyboard.api.algorithm.guide.model.AlgorithmGuide;
import com.honeyboard.api.algorithm.guide.service.AlgorithmGuideService;
import com.honeyboard.api.common.response.PageResponse;
import com.honeyboard.api.user.model.CurrentUser;
import com.honeyboard.api.user.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/algorithm/guide")
@RequiredArgsConstructor
@Slf4j
public class AlgorithmGuideController {

    private final AlgorithmGuideService algorithmGuideService;

    @GetMapping
    public ResponseEntity<?> getAllAlgorithmGuide(
    		@RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "8") int size,
            @RequestParam(required = false) Integer generationId,
            @RequestParam(required = false) String searchType,
            @RequestParam(required = false) String keyword,
            @CurrentUser User user) {
        log.info("알고리즘 개념 전체 조회 요청 - 기수: {}, 검색어: {}", generationId, searchType, keyword);
        
        if(generationId==null) {
        	generationId = user.getGenerationId();
        }
        PageResponse<AlgorithmGuide> algorithmGuides;
        if(keyword != null) {
        	algorithmGuides = algorithmGuideService.searchAlgorithmGuide(page, size, generationId, searchType, keyword);
        }else {
        	algorithmGuides = algorithmGuideService.getAlgorithmGuides(page, size, generationId);        	
        }

        if (algorithmGuides==null) {
            log.info("알고리즘 개념 전체 조회 완료 - 데이터 없음");
            return ResponseEntity.noContent().build();
        }

        log.info("알고리즘 개념 전체 조회 완료 - 조회된 개수: {}", algorithmGuides.getContent().size());
        return ResponseEntity.ok(algorithmGuides);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlgorithmGuide> getAlgorithmGuideDetail(
            @PathVariable int id,
            @RequestParam("bookmark") boolean bookmarkflag) {
        log.info("알고리즘 개념 상세 조회 요청 - ID: {}, 북마크: {}", id, bookmarkflag);
        AlgorithmGuide algorithmGuide = algorithmGuideService.getAlgorithmGuideDetail(id, bookmarkflag);
        
        if (algorithmGuide == null) {
            log.info("알고리즘 개념 상세 조회 완료 - 데이터 없음");
            return ResponseEntity.noContent().build();
        }
        
        log.info("알고리즘 개념 상세 조회 완료 - ID: {}", id);
        return ResponseEntity.ok(algorithmGuide);
    }

    @PostMapping
    public ResponseEntity<?> addAlgorithmGuide(
		    @RequestParam("generationId") int generationId,
		    @RequestBody AlgorithmGuide algorithmGuide) {
	    log.info("알고리즘 개념 작성 요청 - 기수: {}, 알고리즘: {}", generationId, algorithmGuide);
	    boolean result = algorithmGuideService.addAlgorithmGuide(generationId, algorithmGuide);
	    if (result) {
	        log.info("알고리즘 개념 작성 완료");
	        return ResponseEntity.status(HttpStatus.CREATED).build();
	    } else {
	        log.error("알고리즘 개념 작성 실패");
	        return ResponseEntity.badRequest().build();
	    }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateAlgorithmGuide(
            @PathVariable int id,
            @RequestBody AlgorithmGuide algorithmGuide) {
        log.info("알고리즘 개념 수정 요청 - ID: {}", id);
        algorithmGuideService.updateAlgorithmGuide(id, algorithmGuide);
        log.info("알고리즘 개념 수정 완료 - ID: {}", id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> softDeleteAlgorithmGuide(@PathVariable int id) {
        log.info("알고리즘 개념 삭제 요청 - ID: {}", id);
        algorithmGuideService.softDeleteAlgorithmGuide(id);
        log.info("알고리즘 개념 삭제 완료 - ID: {}", id);
        return ResponseEntity.ok().build();
    }

}