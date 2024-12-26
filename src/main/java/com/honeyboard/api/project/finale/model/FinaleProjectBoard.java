package com.honeyboard.api.project.finale.model;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FinaleProjectBoard {
    private int boardId;
    private List<FinaleMember> finaleTeam;
    private String summary;
    private String title;
    private String content;
    private String authorName;
    private LocalDate createdAt;

}
