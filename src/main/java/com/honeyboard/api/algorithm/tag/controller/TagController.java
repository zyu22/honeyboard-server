package com.honeyboard.api.algorithm.tag.controller;

import com.honeyboard.api.algorithm.tag.model.response.TagResponse;
import com.honeyboard.api.algorithm.tag.service.TagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/algorithm/tag")
@Slf4j
public class TagController {
    private final TagService ts;

    // 전체 Tag 조회
//    @GetMapping
//    public ResponseEntity<?> getAllTag() {
//        log.info("태그 전체 조회 시작");
//
//        List<TagResponse> tags = ts.getAllTag();
//
//        log.info("태그 전체 조회 완료 - 총 태그 수: {}", tags.size());
//
//        return ResponseEntity.ok(tags);
//    }

    // Tag 검색
    @GetMapping
    public ResponseEntity<?> searchTag(@RequestParam(value = "keyword") String keyword) {
        log.info("태그 검색 시작 - 키워드: {}", keyword);

        List<TagResponse> tags = ts.searchTag(keyword);

        log.info("태그 검색 완료 - 총 태그 수: {}", tags.size());

        return ResponseEntity.ok(tags);
    }
//
//    @PostMapping
//    public ResponseEntity<?> createTag(@RequestBody TagResponse tag) {
//        try {
//            log.info("태그 생성 요청 - 태그명: {}", tag.getName());
//            // 클라이언트 작동 구현 자체를 조회한 것 기반으로 없으면 추가할거라
//            // 중복 쿼리를 막기 위해 해당 쿼리 존재여부 체크는 백에서 안함
//            // 생성된 Tag를 반환해주면 클라이언트에서 쿼리 아끼고 그대로 문제 생성에 넣을 수 있음
//            TagResponse createdTag = ts.addTag(tag);
//            log.info("태그 생성 완료 - ID: {}, 태그명: {}", createdTag.getId(), createdTag.getName());
//            return ResponseEntity.status(HttpStatus.CREATED).body(createdTag);
//        } catch (Exception e) {
//            log.error("태그 생성 중 에러 발생 - 태그명: {}", tag.getName(), e);
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }
}
