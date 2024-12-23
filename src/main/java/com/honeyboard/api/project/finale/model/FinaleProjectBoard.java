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
public class FinaleProjectBoard {

	private int boardId;
	private List<FinaleMember> finaleTeam;
	private String summary;
	private String title;
	private String content;
	private String authorName;
	private LocalDate createdAt;

}
