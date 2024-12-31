package com.honeyboard.api.algorithm.guide.controller;

import com.honeyboard.api.algorithm.guide.model.AlgorithmGuide;
import com.honeyboard.api.algorithm.guide.service.AlgorithmGuideService;
import com.honeyboard.api.user.model.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/algorithm/guide")
@RequiredArgsConstructor
@Slf4j
public class AlgorithmGuideController {

    private final AlgorithmGuideService algorithmGuideService;

    //알고리즘 개념 전체 조회 (GET /api/v1/algorithm/guide?generation={generationId}) 구현
    @GetMapping
    public ResponseEntity<List<AlgorithmGuide>> getAllAlgorithmGuide(
            @RequestParam(required = false) Integer generationId,
            @RequestParam(required = false) String title,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        int generation = generationId != null ? generationId : userDetails.getUser().getGenerationId();

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
    public ResponseEntity<Void> addAlgorithmGuide(
            @RequestParam("generationId") int generationId,
            @RequestBody AlgorithmGuide algorithmGuide) {

        algorithmGuideService.addAlgorithmGuide(generationId, algorithmGuide);
        return ResponseEntity.status(HttpStatus.CREATED).build();
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
