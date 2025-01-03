package com.honeyboard.api.youtube.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class YoutubeSearchResult {
    private List<Youtube> videos;        // 검색된 동영상 목록
    private String nextPageToken;
}
