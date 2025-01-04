package com.honeyboard.api.user.model.mypage;

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
    private String title;
    private Date createdAt;
}
