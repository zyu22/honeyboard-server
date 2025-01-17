package com.honeyboard.api.project.finale.model;

import com.honeyboard.api.project.finale.model.request.FinaleProjectBoardRequest;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public
class FinaleProjectBoard {

    int id;
    int finaleProjectId;
    int finaleTeamId;
    String summary;
    String title;
    String content;
    int userId;
    String thumbnail;

    public void setInfos(int finaleTeamId, int userId, FinaleProjectBoardRequest fpbr) {
        this.finaleTeamId = finaleTeamId;
        this.userId = userId;
        this.summary = fpbr.getSummary();
        this.title = fpbr.getTitle();
        this.content = fpbr.getContent();
        this.thumbnail = fpbr.getThumbnail();
    }

}
