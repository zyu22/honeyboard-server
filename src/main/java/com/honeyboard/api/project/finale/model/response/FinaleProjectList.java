package com.honeyboard.api.project.finale.model.response;

import com.honeyboard.api.project.model.ProjectUserInfo;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public
class FinaleProjectList {

    int id;
    int finaleTeamId;
    String title;
    String description;
    String thumbnail;
    String createdAt;
    List<ProjectUserInfo> members;  // finaleTeamId에 해당하는 TeamMember
    List<ProjectUserInfo> noTeamUsers; // 팀이 없는 유저
    List<FinaleTeamList> teams; // 팀 리스트

}
