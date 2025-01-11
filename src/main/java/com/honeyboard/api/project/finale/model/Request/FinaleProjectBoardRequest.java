package com.honeyboard.api.project.finale.model.Request;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public
class FinaleProjectBoardRequest {

    String title;
    String summary;
    String content;
    String thumbnail;

}
