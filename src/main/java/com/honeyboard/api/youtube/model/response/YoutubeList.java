package com.honeyboard.api.youtube.model.response;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class YoutubeList {

    private int id;
    private String videoId;
    private String title;
    private String channel;
    private String createdAt;
}
