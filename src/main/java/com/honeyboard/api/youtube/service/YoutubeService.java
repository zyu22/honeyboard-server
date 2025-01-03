package com.honeyboard.api.youtube.service;

import com.honeyboard.api.youtube.model.Youtube;
import com.honeyboard.api.youtube.model.YoutubeSearchResult;

import java.util.List;

public interface YoutubeService {

    YoutubeSearchResult searchVideos(String query, String pageToken);
    void addYoutubeVideo(Youtube youtube);
    List<Youtube> getAllYoutubeVideos(int generationId);
    void deleteYoutubeVideo(int id);
}

