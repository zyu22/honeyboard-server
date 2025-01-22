package com.honeyboard.api.algorithm.tag.service;

import com.honeyboard.api.algorithm.tag.mapper.TagMapper;
import com.honeyboard.api.algorithm.tag.model.response.TagResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TagServiceImp implements TagService {
    private final TagMapper tm;

    // 전체 Tag 조회
//    @Override
//    public List<TagResponse> getAllTag() {
//        log.debug("전체 태그 조회 시작");
//
//        List<TagResponse> tags = tm.selectAllTag();
//
//        if (tags.isEmpty()) {
//            throw new IllegalArgumentException("조회된 태그 정보가 없습니다.");
//        }
//
//        log.debug("전체 태그 조회 완료 - 조회된 태그 수: {}", tags.size());
//        return tags;
//    }
//
    // Tag 검색
    @Override
    public List<TagResponse> searchTag(String keyword) {
        log.info("태그 검색 시작 - 검색어: {}", keyword);
        List<TagResponse> tags = tm.selectSearchTag(keyword);

        log.info("태그 검색 완료 - 검색어: {}, 검색된 태그 수: {}", keyword, tags.size());
        return tags;
    }
//
//    @Override
//    public TagResponse addTag(TagResponse tag) {
//        log.info("태그 생성 시작 - 태그명: {}", tag.getName());
//        int result = tm.insertTag(tag);
//        if (result != 1) {
//            log.error("태그 생성 실패 - 태그명: {}", tag.getName());
//            throw new RuntimeException("태그 생성에 실패했습니다.");
//        }
//        log.info("태그 생성 완료 - ID: {}, 태그명: {}", tag.getId(), tag.getName());
//        return tag;
//    }
}
