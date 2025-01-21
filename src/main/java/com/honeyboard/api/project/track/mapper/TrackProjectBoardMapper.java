package com.honeyboard.api.project.track.mapper;

import com.honeyboard.api.common.model.CreateResponse;
import com.honeyboard.api.project.track.model.request.TrackProjectBoardRequest;
import com.honeyboard.api.project.track.model.response.TrackProjectBoardDetail;
import com.honeyboard.api.project.track.model.response.TrackProjectBoardList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TrackProjectBoardMapper {
    TrackProjectBoardDetail selectTrackProjectBoard(@Param("trackProjectId") Integer trackProjectId, @Param("trackTeamId") Integer trackTeamId, @Param("boardId") Integer boardId);

    int insertTrackProjectBoard(@Param("trackProjectId") int trackProjectId, @Param("trackTeamId") int trackTeamId,
                                @Param("userId") int userId,
                                @Param("board") TrackProjectBoardRequest board,
                                @Param("createResponse") CreateResponse createResponse);

    int updateTrackProjectBoard(@Param("trackProjectId") int trackProjectId, @Param("trackTeamId") int trackTeamId,
                                @Param("boardId") int boardId, @Param("board") TrackProjectBoardRequest board
                                );

    int deleteTrackProjectBoard(@Param("boardId") int boardId);
}
