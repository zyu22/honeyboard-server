package com.honeyboard.api.algorithm.solution.model;

import java.sql.Date;

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

public class AlgorithmSolution {
	
	private int solutionId;
	private int problemId;
	private String title;
	private String summary;
	private String content;
	private int userId;
	private int runtime;
	private int memory;
	private int languageId;
	private int generationId;
	private Date createdAt;
	private Date updatedAt;
	private boolean isDeleted;

}
