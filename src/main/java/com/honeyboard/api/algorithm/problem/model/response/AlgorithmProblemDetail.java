package com.honeyboard.api.algorithm.problem.model.response;


import com.honeyboard.api.algorithm.solution.model.response.AlgorithmSolutionList;
import com.honeyboard.api.algorithm.tag.model.response.TagResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AlgorithmProblemDetail {
    private int id;
    private String title;
    private String url;
    private List<AlgorithmSolutionList> algorithmSolutionList;
    private List<TagResponse> tags;
}
