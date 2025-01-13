package com.honeyboard.api.algorithm.tag.controller;

import com.honeyboard.api.algorithm.tag.service.TagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/algorithm/tag")
@Slf4j
public class TagController {
    private final TagService ts;

//    @GetMapping
//    public ResponseEntity<?> getAllTag(@RequestParam(required = false) String name) {
//        try {
//            List<TagResponse> list;
//            if (name == null || name.isEmpty()) {
//                log.info("전체 태그 조회 요청");
//                list = ts.getAllTag();
//            } else {
//                log.info("태그 검색 요청 - 검색어: {}", name);
//                list = ts.searchTag(name);
//            }
//            log.debug("태그 조회 결과 - 태그 수: {}", list.size());
//            return ResponseEntity.ok().body(list);
//        } catch (Exception e) {
//            log.error("태그 조회 중 에러 발생", e);
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }
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
