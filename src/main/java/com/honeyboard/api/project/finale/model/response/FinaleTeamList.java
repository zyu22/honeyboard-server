package com.honeyboard.api.project.finale.model.response;

import com.honeyboard.api.project.model.TeamMemberInfo;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public
class FinaleTeamList {

    List<TeamMemberInfo> members;
    boolean submitted;
    Integer projectBoardId; // null || 숫자 값 보내줘야해서 객체

}
