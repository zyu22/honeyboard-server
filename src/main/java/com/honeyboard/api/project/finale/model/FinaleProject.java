package com.honeyboard.api.project.finale.model;

import java.time.LocalDate;

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
public class FinaleProject {

	private int finaleProjectId;
	private int teamId;
	private String url;
	private String title;
	private String content;
	private int userId;
	private String name;
	private LocalDate createdAt;
	private LocalDate updatedAt;

}
