package com.honeyboard.api.project.track.model.response;

import com.honeyboard.api.project.model.ProjectUserInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrackProjectDetail {
    private int id;
    private String title;
    private String objective;
    private String description;
    private String createdAt;
    private List<ProjectUserInfo> noTeamUsers;
    private List<TrackTeamList> teams;
    private List<TrackProjectBoardList> trackProjectBoardList;
}
