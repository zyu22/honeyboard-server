package com.honeyboard.api.algorithm.tag.service;

import com.honeyboard.api.algorithm.tag.mapper.TagMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TagServiceImp implements TagService {
    private final TagMapper tm;

//    @Override
//    public List<TagResponse> getAllTag() {
//        log.debug("전체 태그 조회 시작");
//        List<TagResponse> tags = tm.selectAllTag();
//        log.debug("전체 태그 조회 완료 - 조회된 태그 수: {}", tags.size());
//        return tags;
//    }
//
//    @Override
//    public List<TagResponse> searchTag(String input) {
//        log.debug("태그 검색 시작 - 검색어: {}", input);
//        List<TagResponse> tags = tm.selectSearchTag(input);
//        log.debug("태그 검색 완료 - 검색어: {}, 검색된 태그 수: {}", input, tags.size());
//        return tags;
//    }
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
