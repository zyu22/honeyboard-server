package com.honeyboard.api.project.finale.model;

import com.honeyboard.api.project.model.TeamMemberInfo;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public
class FinaleProjectDetail {

    int id;
    int finaleTeamId;
    String title;
    String description;
    String url;
    String createdAt;
    List<TeamMemberInfo> members;
    List<FinaleProjectBoardList> boards;

}
