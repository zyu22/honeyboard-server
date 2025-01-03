package com.honeyboard.api.algorithm.solution.model;

import lombok.*;

import java.sql.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlgorithmSolution {
	
	private int solutionId;
	private int problemId;
	private String title;
	private String summary;
	private String content;
	private int userId;
	private String Author;
	private int runtime;
	private int memory;
	private int languageId;
	private int generationId;
	private Date createdAt;
	private Date updatedAt;
	private boolean isDeleted;
	private boolean isBookmarked;

}
