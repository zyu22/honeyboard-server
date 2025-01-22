package com.honeyboard.api.project.model;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public
class TeamMemberInfo {
    private int id;
    private String name;
    private String role;
}
