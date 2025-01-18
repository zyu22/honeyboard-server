package com.honeyboard.api.youtube.service;

import com.honeyboard.api.youtube.model.request.YoutubeCreate;
import com.honeyboard.api.youtube.model.response.YoutubeList;
import com.honeyboard.api.youtube.model.response.YoutubeResponse;

import java.util.List;

public interface YoutubeService {

    // 유튜브 검색 결과 조회
    YoutubeResponse searchVideos(String query, String pageToken);
    // 유튜브 영상 저장
    void addYoutubeVideo(YoutubeCreate youtube);
    // 플레이리스트 조회
    List<YoutubeList> getAllYoutubeVideos();
    // 플레이리스트 삭제
    void deleteYoutubeVideo(int id);
}

