package com.honeyboard.api.project.finale.model.request;

import com.honeyboard.api.project.model.TeamRequest;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public
class FinaleProjectCreate {

    String title;
    String description;
    String url;
    TeamRequest teams;

}
