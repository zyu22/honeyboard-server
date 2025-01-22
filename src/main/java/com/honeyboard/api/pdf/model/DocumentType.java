package com.honeyboard.api.pdf.model;

public enum DocumentType {
	FINALE("최종 프로젝트", "finale_project_board"), TRACK("트랙 프로젝트", "track_project_board");

	private final String description;
	private final String tableName;

	DocumentType(String description, String tableName) {
		this.description = description;
		this.tableName = tableName;
	}

	public String getDescription() {
		return description;
	}

	public String getTableName() {
		return tableName;
	}
}