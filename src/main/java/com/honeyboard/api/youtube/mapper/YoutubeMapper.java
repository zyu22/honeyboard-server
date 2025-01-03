package com.honeyboard.api.youtube.mapper;

import com.honeyboard.api.youtube.model.Youtube;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface YoutubeMapper {

    int insertYoutubeVideo(Youtube youtube);
    List<Youtube> selectAllYoutubeVideo(int generationId);
    int deleteYoutubeVideo(int id);
    int existsByVideoId(String videoId);
}
