package com.honeyboard.api.project.track.model.response;

import com.honeyboard.api.project.model.TeamMemberInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrackTeamList {
    private int id;
    private List<TeamMemberInfo> members;
    boolean submitted;
    Integer projectBoardId;
}
