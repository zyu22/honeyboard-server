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

	private Integer teamId;
	private String projectName;
	private String summary;
	private Integer generationId;
	private boolean isCompleted;
	private LocalDate createdAt;
	private LocalDate updatedAt;
	private List<FinaleMember> members;

}
