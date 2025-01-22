package com.honeyboard.api.youtube.model.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class YoutubeResponse {

    private List<YoutubeList> youtubeList;
    private String nextPageToken;
}
