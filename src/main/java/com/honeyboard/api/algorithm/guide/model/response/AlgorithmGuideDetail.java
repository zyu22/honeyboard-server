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
    private String title;
    private String content;
    private boolean bookmarked;
}
