package com.honeyboard.api.algorithm.guide.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AlgorithmGuideUpdate {
    private String title;
    private String content;
    private String thumbnail;
    private String updatedAt;
}
