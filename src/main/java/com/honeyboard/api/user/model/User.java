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
    private Date createdAt;

    public User(String name) {
        this.name = name;
    }
}
