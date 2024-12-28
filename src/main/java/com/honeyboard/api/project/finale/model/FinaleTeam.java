package com.honeyboard.api.project.finale.model;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FinaleTeam {

	private int teamId;
	private int generationId;
	private LocalDate submittedAt;
	private LocalDate createdAt;

}
