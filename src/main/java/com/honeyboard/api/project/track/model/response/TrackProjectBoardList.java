package com.honeyboard.api.project.track.model.response;

import com.honeyboard.api.project.model.ProjectUserInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrackProjectBoardList {
    private int id;
    private String title;
    private String createdAt;
    private String thumbnail;
    List<ProjectUserInfo> members;
}
