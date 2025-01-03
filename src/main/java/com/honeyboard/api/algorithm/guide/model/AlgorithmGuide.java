package com.honeyboard.api.algorithm.guide.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlgorithmGuide {

    private int id;
    private String title;
    private String content;
    private String thumbnail;
    private int userId;
    private int generationId;
    private String createdAt;
    private String updatedAt;
    private boolean isDeleted;
}
