package com.honeyboard.api.project.track.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrackProjectRequest {
    private int id;
    private String title;
    private String objective;
    private String description;
    private List<Integer> excludedMembers;
}
