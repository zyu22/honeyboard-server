package com.honeyboard.api.project.finale.model;

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