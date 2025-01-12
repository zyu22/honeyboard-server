package com.honeyboard.api.web.recommend.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WebRecommendList {
    private int id;
    private String title;
    private String url;
    private String createdAt;
}
