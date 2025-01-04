package com.honeyboard.api.algorithm.solution.model;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlgorithmSolutionResponse {
	
	private int solutionId;
	private String title;
	private String summary;
	private String content;
	private int userId;
	private String author;
	private int runtime;
	private int memory;
	private int languageId;
	private boolean bookmark;

}
