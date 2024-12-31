package com.honeyboard.api.web.recommend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WebRecommend {
    private  int id;
    private String title;
    private String url;
    private String content;
    private int userId;
    private int generationId;
    private String createdAt;
    private String updatedAt;
    private boolean isDeleted;
}
