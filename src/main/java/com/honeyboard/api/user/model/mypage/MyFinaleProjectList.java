package com.honeyboard.api.user.model.mypage;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class MyFinaleProjectList {
    private int id;
    private String title;
    private String createdAt;
}
