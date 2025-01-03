package com.honeyboard.api.user.service;

import com.honeyboard.api.user.model.MyAlgorithmSolution;
import com.honeyboard.api.user.model.MyFinaleProject;
import com.honeyboard.api.user.model.MyTrackProject;

import java.util.List;

public interface MyPageService {
    List<MyTrackProject> getAllMyTrackProjects(int userId);

    List<MyFinaleProject> getAllMyFinaleProjects(int userId);

    List<MyAlgorithmSolution> getAllMyAlgorithms(int userId);
}
