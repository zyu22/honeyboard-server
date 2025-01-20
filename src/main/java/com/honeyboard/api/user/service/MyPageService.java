package com.honeyboard.api.user.service;

import com.honeyboard.api.user.model.mypage.MyAlgorithmSolutionList;
import com.honeyboard.api.user.model.mypage.MyFinaleProjectList;
import com.honeyboard.api.user.model.mypage.MyTrackProjectList;

import java.util.List;

public interface MyPageService {
    List<MyTrackProjectList> getAllMyTrackProjects(int userId);

    List<MyFinaleProjectList> getAllMyFinaleProjects(int userId);

    List<MyAlgorithmSolutionList> getAllMyAlgorithms(int userId);
}
