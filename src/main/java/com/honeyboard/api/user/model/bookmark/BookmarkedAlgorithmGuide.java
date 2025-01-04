package com.honeyboard.api.user.model.bookmark;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BookmarkedAlgorithmGuide {
    private int id;
    private String title;
    private String thumbnail;
    private String createdAt;
}
