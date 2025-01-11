package com.honeyboard.api.algorithm.problem.model.response;


import com.honeyboard.api.algorithm.solution.model.response.AlgorithmSolutionList;
import io.opencensus.tags.Tags;
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
    private List<Tags> tags;
}
