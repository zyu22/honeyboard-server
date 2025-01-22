package com.honeyboard.api.algorithm.problem.model.response;


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
public class AlgorithmProblemList {
    private int id;
    private String title;
    private String url;
    private String createdAt;
    private List<TagResponse> tags;
}
