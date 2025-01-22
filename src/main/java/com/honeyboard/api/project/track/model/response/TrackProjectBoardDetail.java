package com.honeyboard.api.project.track.model.response;

import com.honeyboard.api.project.model.TeamMemberInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

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
    private List<TeamMemberInfo> members;
}
