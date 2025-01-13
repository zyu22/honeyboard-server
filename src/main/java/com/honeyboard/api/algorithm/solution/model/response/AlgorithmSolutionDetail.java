package com.honeyboard.api.algorithm.solution.model.response;

import com.honeyboard.api.bookmark.model.BookmarkResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AlgorithmSolutionDetail {
    private int id;
    private String title;
    private String summary;
    private String content;
    private String name;
    private int runtime;
    private int memory;
    private int languageId;
    private String languageName;
    private BookmarkResponse bookmarked;
    private String createdAt;
}
