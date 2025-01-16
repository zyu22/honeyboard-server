package com.honeyboard.api.project.track.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrackProjectBoardRequest {
    private String title;
    private String url;
    private String content;
    private String thumbnail;
}
