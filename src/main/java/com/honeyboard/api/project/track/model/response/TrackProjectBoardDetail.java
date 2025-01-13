package com.honeyboard.api.project.track.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrackProjectBoardDetail {
    private int id;
    private int trackTeamId;
    private String title;
    private String url;
    private String content;
    private String createdAt;
}
