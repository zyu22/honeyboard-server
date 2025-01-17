package com.honeyboard.api.web.recommend.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WebRecommendRequest {
    private String title;
    private String url;
    private String content;
}
