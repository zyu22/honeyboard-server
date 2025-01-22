package com.honeyboard.api.algorithm.solution.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AlgorithmSolutionList {

    private int id;
    private int languageId;
    private int memory;
    private int runtime;
    private String title;
    private String subtitle;
    private String languageName;
}
