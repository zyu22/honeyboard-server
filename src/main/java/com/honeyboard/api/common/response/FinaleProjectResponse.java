package com.honeyboard.api.common.response;

import com.honeyboard.api.project.finale.model.FinaleProject;
import com.honeyboard.api.project.finale.model.FinaleTeam;
import com.honeyboard.api.user.model.User;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FinaleProjectResponse {
    private List<FinaleProject> projects;
    private List<User> users;
    private List<FinaleTeam> teams;
}