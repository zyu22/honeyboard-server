package com.honeyboard.api.user.service;

import com.honeyboard.api.user.model.mypage.MyAlgorithmSolution;
import com.honeyboard.api.user.model.mypage.MyFinaleProject;
import com.honeyboard.api.user.model.mypage.MyTrackProject;

import java.util.List;

public interface MyPageService {
    List<MyTrackProject> getAllMyTrackProjects(int userId);

    List<MyFinaleProject> getAllMyFinaleProjects(int userId);

    List<MyAlgorithmSolution> getAllMyAlgorithms(int userId);
}
