package com.honeyboard.api.youtube.model.request;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class YoutubeCreate {

    private String videoId;
    private String title;
    private String channel;
    private String  createdAt;
}
