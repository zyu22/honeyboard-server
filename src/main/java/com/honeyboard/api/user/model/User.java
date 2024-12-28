package com.honeyboard.api.user.model;

import lombok.*;

import java.sql.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    private int userId;
    private String email;
    private String password;
    private String name;
    private int generationId;
    private String role;
    private String loginType;
    private boolean isSsafy;
    private int teamId;
    private Date createdAt;

    public User(String name) {
        this.name = name;
    }

    public User(int userId, String name, int generationId, int teamId) {
        this.userId = userId;
        this.name = name;
        this.generationId = generationId;
        this.teamId = teamId;
    }
}
