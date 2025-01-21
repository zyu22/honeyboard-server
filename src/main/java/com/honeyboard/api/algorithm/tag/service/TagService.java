package com.honeyboard.api.algorithm.tag.service;

import com.honeyboard.api.algorithm.tag.model.response.TagResponse;

import java.util.List;

public interface TagService {

    // 전체 Tag 조회
    //List<TagResponse> getAllTag();

    // Tag 검색
    List<TagResponse> searchTag(String keyword);
//
//    TagResponse addTag(TagResponse tag);
}
