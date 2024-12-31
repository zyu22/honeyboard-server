package com.honeyboard.api.project.track.model;

import com.honeyboard.api.user.model.User;
import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TrackTeamMember {
    private int trackTeamId;
    private int trackMemberId;
    private User user;
    private LocalDate createdAt;
    private TeamRole role;
}
