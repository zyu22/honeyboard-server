package com.honeyboard.api.pdf.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.honeyboard.api.project.finale.model.response.FinaleProjectBoardDetail;
import com.honeyboard.api.project.track.model.response.TrackProjectBoardDetail;
import com.honeyboard.api.project.track.model.response.TrackProjectBoardDetailWithTeam;
import com.honeyboard.api.user.model.User;

import com.honeyboard.api.user.model.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
//@AllArgsConstructor
public class Document {
//    private Integer id;
//
//    // 공통 필드
//    private String title;
//    private String content;
//    private String authorName;
//    private LocalDate createdAt;
//    private DocumentType documentType;
//    private String summary;
//    private String url;
//
//    @Builder.Default
//    private List<User> teamMembers = new ArrayList<>();
//    private String teamName;
//
//    public static Document fromFinaleProject(FinaleProjectBoardDetail board) {
//        return Document.builder()
//                .id(board.getId())
//                .title(board.getTitle())
//                .content(board.getContent())
//                .teamMembers(parseTeamMembers(board.getMembers()))
//                .createdAt(board.getCreatedAt())
//                .summary(board.getSummary())
//                .documentType(DocumentType.FINALE)
//                .build();
//    }
//
//    public static Document fromTrackProject(TrackProjectBoardDetailWithTeam board) {
//        return Document.builder()
//                .id(board.getId())
//                .title(board.getTitle())
//                .content(board.getContent())
//                .url(board.getUrl())
//                .teamMembers(parseTeamMembers(board.getMembers()))
//                .documentType(DocumentType.TRACK)
//                .createdAt(LocalDate.parse(board.getCreatedAt().split(" ")[0]))
//                .build();
//    }
//
//    private static List<User> parseTeamMembers(List<UserInfo> teamMembers) {
//        try {
//            if (teamMembers != null && !teamMembers.isEmpty()) {
//                return teamMembers.stream()
//                        .map(userInfo -> {
//                            User user = new User();
//                            user.setUserId(userInfo.getUserId());
//                            user.setName(userInfo.getName());
//                            return user;
//                        })
//                        .collect(Collectors.toList());
//            }
//            return new ArrayList<>();
//        } catch (Exception e) {
//            throw new IllegalArgumentException("팀 멤버 파싱 중 오류가 발생했습니다.", e);
//        }
//    }


}
