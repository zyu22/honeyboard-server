package com.honeyboard.api.algorithm.guide.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AlgorithmGuide {
    private int id;
    private String title;
    private String content;
    private String thumbnail;
    private int userId;
    private int generationId;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    private boolean deleted;
    private boolean bookmarked; // 북마크 여부 확인
}
