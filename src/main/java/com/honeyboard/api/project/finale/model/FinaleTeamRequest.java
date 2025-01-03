package com.honeyboard.api.project.finale.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FinaleTeamRequest {
    private Integer teamId;
    private Integer generationId;
    private Integer leaderId;
    private List<Integer> memberIds;
}
