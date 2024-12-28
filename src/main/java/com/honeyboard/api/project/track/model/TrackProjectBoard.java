package com.honeyboard.api.project.track.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrackProjectBoard {
    private int id;
    private int trackProjectId;
    private int trackTeamId;
    private String title;
    private String url;
    private String content;
    private String createdAt;
    private String updatedAt;
    private boolean isDeleted;

}
