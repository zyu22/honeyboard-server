package com.honeyboard.api.pdf;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.honeyboard.api.project.finale.model.FinaleProjectBoard;
import com.honeyboard.api.user.model.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Document {
	private Integer id; // 문서 ID

	// 공통 필드
	private String title; // 프로젝트 제목
	private String content; // 프로젝트 내용
	private String userName; // 작성자 이름
	private LocalDate createdAt; // 생성일
	private DocumentType documentType; // 문서 타입
	private String summary; // 요약 (finale)
	private String url; // gitlab 주소

	@Builder.Default
	private List<User> teamMembers = new ArrayList<>(); // 팀원 목록
	private String teamName; // 팀 이름

	public static Document fromFinaleProject(FinaleProjectBoard board) {
		return Document.builder().id(board.getId()).projectId(board.getProjectId()).teamId(board.getTeamId())
				.summary(board.getSummary()).title(board.getTitle()).content(board.getContent())
				.userId(board.getUserId()).createdAt(board.getCreatedAt()).updatedAt(board.getUpdatedAt())
				.isDeleted(board.getIsDeleted()).documentType(DocumentType.FINALE).build();
	}

	public static Document fromTrackProject(TrackProjectBoard board) {
		return Document.builder().id(board.getId()).trackProjectId(board.getTrackProjectId())
				.trackTeamId(board.getTrackTeamId()).url(board.getUrl()).title(board.getTitle())
				.content(board.getContent()).userId(board.getUserId()).createdAt(board.getCreatedAt())
				.updatedAt(board.getUpdatedAt()).isDeleted(board.getIsDeleted()).documentType(DocumentType.TRACK)
				.build();
	}

	public void setAdditionalInfo(String teamName, List<String> teamMembers, String authorName) {
		this.teamName = teamName;
		this.teamMembers = teamMembers;
		this.userName = authorName;
	}
}