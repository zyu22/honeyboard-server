package com.honeyboard.api.user.model.bookmark;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BookmarkedAlgorithmSolution {

    // 알고리즘 풀이 ID
    private int solutionId;
    // 풀이 제목
    private String solutionTitle;
    //작성자
    private String author;

    // 실행 시간
    private int runtime;

    // 메모리
    private int memory;
    // 사용 언어 ID
    private int languageId;

}
