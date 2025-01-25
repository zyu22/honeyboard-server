package com.honeyboard.api.project.finale.service;


import com.honeyboard.api.project.finale.model.request.FinaleProjectBoardRequest;
import com.honeyboard.api.project.finale.model.response.FinaleProjectBoardDetail;
import com.honeyboard.api.user.model.User;


public interface FinaleProjectBoardService {

    FinaleProjectBoardDetail getFinaleProjectBoardDetail(int finaleProjectId, int boardId);

    int createFinaleProjectBoard(int finaleProjectId, FinaleProjectBoardRequest request, User currentUser);

    boolean updateFinaleProjectBoard(int finaleProjectId, int finaleProjectBoardId, FinaleProjectBoardRequest request, User currentUser);

    boolean deleteFinaleProjectBoard(int finaleProjectId, int finaleProjectBoardId, User currentUser);
}
