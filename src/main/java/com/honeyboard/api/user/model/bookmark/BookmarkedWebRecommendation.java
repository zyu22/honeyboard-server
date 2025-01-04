package com.honeyboard.api.user.model.bookmark;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BookmarkedWebRecommendation {

    private int id;
    private String title;
    private String url;
    private String createdAt;

}
