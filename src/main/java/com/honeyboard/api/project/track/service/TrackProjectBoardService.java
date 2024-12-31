package com.honeyboard.api.project.track.service;

import com.honeyboard.api.project.track.model.TrackProject;
import com.honeyboard.api.project.track.model.TrackProjectBoard;

import java.util.List;

public interface TrackProjectBoardService {
    List<TrackProjectBoard> getAllBoard(int trackProjectId);
    TrackProjectBoard getBoard(int boardId);
    TrackProjectBoard addBoard(int trackProjectId, TrackProjectBoard board);
    TrackProjectBoard updateBoard(int boardId, TrackProjectBoard board);
    boolean softDeleteBoard(int boardId);
}
