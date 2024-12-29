package com.honeyboard.api.project.track.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TeamMemberInfo {
    private int userId;
    private String userName;
    private String role;
}
