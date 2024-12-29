package com.honeyboard.api.project.track.model;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TrackTeam {
    private int id;
    private int generationId;
    private int trackProjectId;
    private List<TrackTeamMember> members;
    private LocalDate createdAt;
    private LocalDate updatedAt;
}
