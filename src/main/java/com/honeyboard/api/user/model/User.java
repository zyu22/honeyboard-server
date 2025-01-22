package com.honeyboard.api.user.model;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    private int userId = 0;
    private String email;
    private String password;
    private String name;
    private int generationId = 0;
    private int generationName = 0;
    private String role;
    private String loginType;
    private Boolean isSsafy;
    private int teamId = 0;
    private LocalDate createdAt;

    public User(String name) {
        this.name = name;
    }

    public User(int userId, String name, int generationId, int teamId) {
        this.userId = userId;
        this.name = name;
        this.generationId = generationId;
        this.teamId = teamId;
    }

    public User(int userId, String name, int generationId, int generationName, String role) {
        this.userId = userId;
        this.name = name;
        this.generationId = generationId;
        this.generationName = generationName;
        this.role = role;
    }

    public int getGenerationId() {
        return this.generationId;
    }
}
