package com.honeyboard.api.user.model.bookmark;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BookmarkedWebGuide {

    private int id;
    private String title;
    private String createdAt;
    private String thumbnail;

}
