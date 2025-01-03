package com.honeyboard.api.user.model;

import com.honeyboard.api.project.track.model.TrackTeamMember;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyTrackProject {

    // 트랙 프로젝트(상위)
    private int trackProjectId;
    private String trackProjectTitle;

    // 하위 프로젝트(board)
    private int trackProjectBoardId;
    private String trackProjectBoardTitle;

    // 참여 팀원들 목록 (자신 포함)
    private int trackTeamId;
    private List<TrackTeamMember> teamMembers;
}
