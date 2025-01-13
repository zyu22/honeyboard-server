package com.honeyboard.api.project.track.mapper;

import com.honeyboard.api.project.track.model.request.TrackProjectBoardRequest;
import com.honeyboard.api.project.track.model.response.TrackProjectBoardDetail;
import com.honeyboard.api.project.track.model.response.TrackProjectBoardList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TrackProjectBoardMapper {
    TrackProjectBoardDetail selectTrackProjectBoard(@Param("boardId") Integer boardId);

    int insertTrackProjectBoard(@Param("trackProjectId") int trackProjectId, @Param("trackTeamId") int trackTeamId,
                                @Param("board") TrackProjectBoardRequest board);

    int updateTrackProjectBoard(@Param("trackProjectId") int trackProjectId, @Param("trackTeamId") int trackTeamId,
                                @Param("boardId") int boardId, @Param("board") TrackProjectBoardRequest board);

    int deleteTrackProjectBoard(@Param("boardId") Integer boardId);
}
