package com.honeyboard.api.project.track.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class TrackProjectStatus {
    private int projectId;
    private String projectName;
    private List<TeamStatus> teams;
    private List<TeamMemberInfo> noTeamMembers;
    private int totalTeams;
    private int completedTeams;
    private int noTeamMembersCount;
}