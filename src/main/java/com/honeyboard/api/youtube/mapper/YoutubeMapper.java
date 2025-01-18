package com.honeyboard.api.youtube.mapper;

import com.honeyboard.api.youtube.model.request.YoutubeCreate;
import com.honeyboard.api.youtube.model.response.YoutubeList;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface YoutubeMapper {

    // 유튜브 영상 저장
    int insertYoutubeVideo(YoutubeCreate youtube);
    // 플레이리스트 조회
    List<YoutubeList> selectAllYoutubeVideo();
    // 플레이리스트 삭제
    int deleteYoutubeVideo(int id);
    //플레이리스트 중복 조회
    int existsByVideoId(String videoId);
}
