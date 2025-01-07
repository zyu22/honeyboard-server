package com.honeyboard.api.algorithm.problem.model.request;


import com.honeyboard.api.algorithm.tag.model.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AlgorithmProblemRequest {
    private String title;
    private String url;
    private List<Tag> tags;
}
