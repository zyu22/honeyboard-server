package com.honeyboard.api.user.model.mypage;

import com.honeyboard.api.project.model.TeamMemberInfo;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyTrackProjectList {
    private int boardId;
    private int trackProjectId;
    private int trackTeamId;
    private String title;
    private String thumbnail;
    private String trackProjectName;
    private List<TeamMemberInfo> trackTeam;
}
