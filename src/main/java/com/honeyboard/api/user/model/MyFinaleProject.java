package com.honeyboard.api.user.model;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class MyFinaleProject {
    private int finaleProjectBoardId;
    private int finaleTeamId;
    private String title;
    private Date createdAt;
}
