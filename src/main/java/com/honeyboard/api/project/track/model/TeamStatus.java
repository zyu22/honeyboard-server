package com.honeyboard.api.project.track.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TeamStatus {
    private int teamId;
    private boolean isCompleted;
    private List<TeamMemberInfo> members;
}
