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
class FinaleProjectBoardDetail {

    int id;
    String title;
    String summary;
    String content;
    String createdAt;
    List<TeamMemberInfo> members;

}
