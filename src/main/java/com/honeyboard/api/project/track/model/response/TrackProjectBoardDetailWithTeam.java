package com.honeyboard.api.project.track.model.response;

import com.honeyboard.api.user.model.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrackProjectBoardDetailWithTeam {
    private int id;
    private int trackTeamId;
    private String title;
    private String url;
    private String content;
    private
    List<UserInfo> members;
    private String createdAt;
}
