package com.honeyboard.api.project.track.mapper;

import com.honeyboard.api.project.track.model.TrackProjectBoard;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TrackProjectBoardMapper {
    List<TrackProjectBoard> selectAllTrackProjectsBoards(@Param("trackProjectId") Integer trackProjectId);
    TrackProjectBoard selectTrackProjectBoard(@Param("boardId") Integer boardId);
    int insertTrackProjectBoard(@Param("trackProjectId") Integer trackProjectId, @Param("trackProjectBoard") TrackProjectBoard trackProjectBoard);
    int updateTrackProjectBoard(@Param("boardId") Integer boardId, @Param("trackProjectBoard") TrackProjectBoard trackProjectBoard);
    int deleteTrackProjectBoard(@Param("boardId") Integer boardId);
}
