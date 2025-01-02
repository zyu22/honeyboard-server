package com.honeyboard.api.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {
    private int userId;
    private String name;
    private String email;
    private boolean ssafy;
    private int generationId;
}
