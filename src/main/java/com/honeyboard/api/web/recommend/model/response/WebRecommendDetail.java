package com.honeyboard.api.web.recommend.model.response;

import com.honeyboard.api.bookmark.model.BookmarkResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WebRecommendDetail {
    private int id;
    private String title;
    private String content;
    private String url;
    private int authorId;
    private String authorName;
    private boolean bookmark;
}
