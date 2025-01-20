package com.honeyboard.api.algorithm.tag.service;

import com.honeyboard.api.algorithm.tag.model.response.TagResponse;

import java.util.List;

public interface TagService {

    // 전체 Tag 조회
    List<TagResponse> getAllTag();

//    List<TagResponse> searchTag(String input);
//
//    TagResponse addTag(TagResponse tag);
}
