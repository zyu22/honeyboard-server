package com.honeyboard.api.youtube.mapper;

import com.honeyboard.api.youtube.model.request.YoutubeCreate;
import com.honeyboard.api.youtube.model.response.YoutubeList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface YoutubeMapper {

    // 유튜브 영상 저장
    int insertYoutubeVideo(YoutubeCreate youtubeCreate, int generationId);
    // 플레이리스트 조회
    List<YoutubeList> selectAllYoutubeVideo(@Param("generationId") int generationId);
    // 플레이리스트 삭제
    int deleteYoutubeVideo(@Param("id") int id);
    //플레이리스트 중복 조회
    int existsByVideoId(@Param("videoId") String videoId, @Param("generationId") int generationId);
}
