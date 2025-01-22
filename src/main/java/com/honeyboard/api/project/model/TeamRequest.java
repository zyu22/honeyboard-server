package com.honeyboard.api.project.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TeamRequest {
    private int id;
    private int leaderId;
    private List<Integer> memberIds;
}
