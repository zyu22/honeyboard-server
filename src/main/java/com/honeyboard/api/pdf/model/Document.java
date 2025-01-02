package com.honeyboard.api.pdf.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.honeyboard.api.project.finale.model.FinaleProjectBoard;
import com.honeyboard.api.project.track.model.TrackProjectBoard;
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
    private Integer id;

    // 공통 필드
    private String title;
    private String content;
    private String authorName;
    private LocalDate createdAt;
    private DocumentType documentType;
    private String summary;
    private String url;

    @Builder.Default
    private List<User> teamMembers = new ArrayList<>();
    private String teamName;

    public static Document fromFinaleProject(FinaleProjectBoard board) {
        return Document.builder()
                .id(board.getBoardId())
                .title(board.getTitle())
                .content(board.getContent())
                .authorName(board.getAuthorName())
                .createdAt(board.getCreatedAt())
                .summary(board.getSummary())
                .documentType(DocumentType.FINALE)
                .build();
    }

    public static Document fromTrackProject(TrackProjectBoard board) {
        return Document.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .url(board.getUrl())
                .teamMembers(parseTeamMembers(board.getTeamMembers()))
                .documentType(DocumentType.TRACK)
                .createdAt(LocalDate.parse(board.getCreatedAt().split(" ")[0]))
                .build();
    }

    private static List<User> parseTeamMembers(String teamMembers) {
        List<User> members = new ArrayList<>();
        if (teamMembers != null && !teamMembers.isEmpty()) {
            String[] memberNames = teamMembers.split(",");
            for (String name : memberNames) {
                members.add(new User(name.trim()));
            }
        }
        return members;
    }
}
