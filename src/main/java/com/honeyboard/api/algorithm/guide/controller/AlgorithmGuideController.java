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
<<<<<<< HEAD
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
=======
    public ResponseEntity<List<AlgorithmGuide>> getAllAlgorithmGuide(
            @RequestParam(required = false) Integer generationId,
            @RequestParam(required = false) String title,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        int generation = generationId != null ? generationId : userDetails.getUser().getGenerationId();
>>>>>>> 7d2e536a36b37daa5362ea6ad61e35748385cbbe

        List<AlgorithmGuide> algorithmGuides = title != null ?
                algorithmGuideService.searchAlgorithmGuide(generation, title) :
                algorithmGuideService.getAlgorithmGuides(generation);

        return algorithmGuides.isEmpty() ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(algorithmGuides);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlgorithmGuide> getAlgorithmGuideDetail(
            @PathVariable int id,
            @RequestParam("bookmark") boolean bookmarkflag) {

        AlgorithmGuide algorithmGuide = algorithmGuideService.getAlgorithmGuideDetail(id, bookmarkflag);
        return algorithmGuide == null ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(algorithmGuide);
    }

    @PostMapping
<<<<<<< HEAD
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
=======
    public ResponseEntity<Void> addAlgorithmGuide(
            @RequestParam("generationId") int generationId,
            @RequestBody AlgorithmGuide algorithmGuide) {

        algorithmGuideService.addAlgorithmGuide(generationId, algorithmGuide);
        return ResponseEntity.status(HttpStatus.CREATED).build();
>>>>>>> 7d2e536a36b37daa5362ea6ad61e35748385cbbe
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateAlgorithmGuide(
            @PathVariable int id,
            @RequestBody AlgorithmGuide algorithmGuide) {

        algorithmGuideService.updateAlgorithmGuide(id, algorithmGuide);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlgorithmGuide(@PathVariable int id) {
        algorithmGuideService.deleteAlgorithmGuide(id);
        return ResponseEntity.ok().build();
    }

}
