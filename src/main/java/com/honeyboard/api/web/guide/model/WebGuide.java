package com.honeyboard.api.web.guide.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WebGuide {
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
