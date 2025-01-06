package com.honeyboard.api.algorithm.solution.model;

import lombok.*;

import java.time.LocalDate;

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
	private LocalDate createdAt;
	private LocalDate updatedAt;
	private boolean isDeleted;
	private boolean bookmark;

}
