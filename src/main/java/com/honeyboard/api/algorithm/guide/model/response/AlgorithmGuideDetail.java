package com.honeyboard.api.algorithm.guide.model.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AlgorithmGuideDetail {
    private int id;
    private String title;
    private String content;
    private String createdAt;
    private int authorId;
    private String authorName;
    private boolean bookmarked;
}
