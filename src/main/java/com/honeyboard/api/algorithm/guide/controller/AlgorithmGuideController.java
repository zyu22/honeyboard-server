package com.honeyboard.api.algorithm.guide.controller;

import com.honeyboard.api.algorithm.guide.model.AlgorithmGuide;
import com.honeyboard.api.algorithm.guide.service.AlgorithmGuideService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/algorithm/guide")
@RequiredArgsConstructor
public class AlgorithmGuideController {

    private final AlgorithmGuideService algorithmGuideService;

    //알고리즘 개념 전체 조회 (GET /api/v1/algorithm/guide?generation={generationId}) 구현
    @GetMapping("/")
    public ResponseEntity<?> getAlgorithmGuides(@RequestParam("generationId") int generationId) {
        try{
            List<AlgorithmGuide> algorithmGuides = algorithmGuideService.getAlgorithmGuides(generationId);
            if(!algorithmGuides.isEmpty()){
                return new ResponseEntity<>(algorithmGuides, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //알고리즘 개념 상세 조회 (GET /api/v1/algorithm/guide/{id}?bookmark={bookmarkflag}) 구현
    @GetMapping("{id}")
    public ResponseEntity<?> getAlgorithmGuideDetail(@PathVariable int id,
                                                     @RequestParam("bookmark") boolean bookmarkflag) {
        try {
            AlgorithmGuide algorithmGuide = algorithmGuideService.getAlgorithmGuideDetail(id, bookmarkflag);
            return ResponseEntity.ok(algorithmGuide);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //알고리즘 개념 검색 (GET /api/v1/algorithm/guide?generation={generationId}&title={title}) 구현
    @GetMapping("/search")
    public ResponseEntity<?> searchAlgorithmGuide(@RequestParam("generationId") int generationId,
                                                       @RequestParam("title") String title) {
        try{
            List<AlgorithmGuide> algorithmGuides = algorithmGuideService.searchAlgorithmGuide(generationId, title);
            if(!algorithmGuides.isEmpty()){
                return new ResponseEntity<>(algorithmGuides, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //알고리즘 개념 작성 (POST /api/v1/algorithm/guide?generation={generationId}) 구현
    @PostMapping("/")
    public ResponseEntity<?> addAlgorithmGuide(@RequestParam("generationId") int generationId,
                                               @RequestBody AlgorithmGuide algorithmGuide) {
        try {
            boolean result = algorithmGuideService.addAlgorithmGuide(generationId, algorithmGuide);
            if (result) {
                return ResponseEntity.status(HttpStatus.CREATED).build();
            }
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
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
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
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
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
