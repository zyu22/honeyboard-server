package com.honeyboard.api.project.track.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrackProject {
    private int id;
    private int trackProjectId;
    private int trackTeamId;
    private String url;
    private String title;
    private String objective; // 목표
    private String content;
    private int generationId; // 기수
    private int userId;
    private String createdAt;
    private String updatedAt;
    private boolean isDeleted;
    private List<Integer> excludedMemberIds;
}
