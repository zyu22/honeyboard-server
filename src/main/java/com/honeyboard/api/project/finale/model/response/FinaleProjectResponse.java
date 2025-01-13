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
class FinaleProjectResponse {

    List<FinaleProjectList> projects;
    List<ProjectUserInfo> noTeamUsers; // 팀이 없는 유저
    List<FinaleTeamList> teams; // 팀 리스트

}
