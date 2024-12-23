package com.honeyboard.api.algorithm.problem.model;


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

public class AlgorithmProblem {
    private int id;
    private String title;
    private String url;
    private int userId;
    private String createdAt;
    private String updatedAt;
    private int isDeleted;
    private List<Tag> tags;
}
