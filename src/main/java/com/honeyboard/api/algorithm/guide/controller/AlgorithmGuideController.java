package com.honeyboard.api.algorithm.guide.controller;

import java.util.List;

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

import com.honeyboard.api.algorithm.guide.model.AlgorithmGuide;
import com.honeyboard.api.algorithm.guide.service.AlgorithmGuideService;
import com.honeyboard.api.user.model.CurrentUser;
import com.honeyboard.api.user.model.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("api/v1/algorithm/guide")
@RequiredArgsConstructor
@Slf4j
public class AlgorithmGuideController {

    private final AlgorithmGuideService algorithmGuideService;

    //알고리즘 개념 전체 조회 (GET /api/v1/algorithm/guide?generation={generationId}) 구현
    @GetMapping
    public ResponseEntity<?> getAllAlgorithmGuide(@RequestParam(required = false) Integer generationId,
                                                  @RequestParam(required = false) String title,
                                                  @CurrentUser User user) {
        try{
            List<AlgorithmGuide> algorithmGuides;
            int generation;

            if(generationId==null) {
                generation = user.getGenerationId();
            }else{
                generation = generationId;
            }

            if (title == null) {
                algorithmGuides = algorithmGuideService.getAlgorithmGuides(generation);
            }else{
                algorithmGuides = algorithmGuideService.searchAlgorithmGuide(generation,title);
            }

            if(algorithmGuides.isEmpty()){
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok().body(algorithmGuides);

        } catch (Exception e) {
            log.error("알고리즘 개념 조회 중 에러 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //알고리즘 개념 상세 조회 (GET /api/v1/algorithm/guide/{id}?bookmark={bookmarkflag}) 구현
    @GetMapping("/{id}")
    public ResponseEntity<?> getAlgorithmGuideDetail(@PathVariable int id,
                                                     @RequestParam("bookmark") boolean bookmarkflag) {
        try {
            AlgorithmGuide algorithmGuide = algorithmGuideService.getAlgorithmGuideDetail(id, bookmarkflag);
            if(algorithmGuide==null){
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok().body(algorithmGuide);
        } catch (Exception e) {
            log.error("알고리즘 개념 상세 조회 중 에러 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //알고리즘 개념 작성 (POST /api/v1/algorithm/guide?generation={generationId}) 구현
    @PostMapping
    public ResponseEntity<?> addAlgorithmGuide(@RequestParam("generationId") int generationId,
                                               @RequestBody AlgorithmGuide algorithmGuide) {
        try {
        	log.debug("요청: generationId={}, algorithmGuide={}", generationId, algorithmGuide);
            boolean result = algorithmGuideService.addAlgorithmGuide(generationId, algorithmGuide);
            if (result) {
                return ResponseEntity.status(HttpStatus.CREATED).build();
            }
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("알고리즘 개념 작성 중 에러 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //알고리즘 개념 수정 (PUT /api/v1/algorithm/guide/{id}) 구현
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAlgorithmGuide(@PathVariable int id,
                                                  @RequestBody AlgorithmGuide algorithmGuide) {
        try {
            boolean result = algorithmGuideService.updateAlgorithmGuide(id, algorithmGuide);
            if (result) {
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("알고리즘 개념 수정 중 에러 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //알고리즘 개념 삭제 (DELETE /api/v1/algorithm/guide/{id}) 구현
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAlgorithmGuide(@PathVariable int id) {
        try {
            boolean result = algorithmGuideService.deleteAlgorithmGuide(id);
            if (result) {
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("알고리즘 개념 삭제 중 에러 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
