package com.honeyboard.api.user.model.mypage;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyAlgorithmSolutionList {
    private int id;
    private String title;
    private String problemTitle;
    private int runtime;
    private int memory;
    private int languageId;
}
