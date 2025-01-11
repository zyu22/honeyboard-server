package com.honeyboard.api.algorithm.solution.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AlgorithmSolutionRequest {
    private String title;
    private String summary;
    private String content;
    private int runtime;
    private int memory;
    private int languageId;
}
