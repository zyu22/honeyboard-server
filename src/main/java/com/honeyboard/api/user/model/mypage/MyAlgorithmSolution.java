package com.honeyboard.api.user.model.mypage;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyAlgorithmSolution {

    // 문제 제목
    private String problemTitle;

    // 알고리즘 풀이 ID
    private int solutionId;

    // 풀이 제목
    private String solutionTitle;

    // 실행 시간
    private int runtime;

    // 메모리
    private int memory;

    // 사용 언어 ID
    private int languageId;
}
