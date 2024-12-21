package com.honeyboard.api.algorithm.problem.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
}
