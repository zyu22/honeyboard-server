package com.honeyboard.api.team.track.model;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrackTeam {
	private int id;
	private int generationId;
	private int trackProjectId;
	private boolean isCompleted;
	private LocalDate createdAt;
	private LocalDate updatedAt;

}
