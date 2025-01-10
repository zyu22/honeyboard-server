package com.honeyboard.api.algorithm.guide.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AlgorithmGuideList {
    private int id;
    private String title;
    private String thumbnail;
    private String createdAt;
}
