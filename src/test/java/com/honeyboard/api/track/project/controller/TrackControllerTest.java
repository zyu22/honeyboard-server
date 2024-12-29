package com.honeyboard.api.track.project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.honeyboard.api.project.track.model.*;
import com.honeyboard.api.user.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)  // 시큐리티 필터 비활성화
public class TrackControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;  // JSON 변환을 위한 매퍼


    @Test
    @WithMockUser
    void getTeamStatus() throws Exception {
        // given
        int generationId = 13;
        int projectId = 2;

        // when
        MvcResult result = mockMvc.perform(get("/api/v1/project/track/{projectId}/status", projectId)
                        .param("generation", String.valueOf(generationId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        // then
        String responseBody = result.getResponse().getContentAsString();
        TrackProjectStatus response = objectMapper.readValue(responseBody, TrackProjectStatus.class);

        System.out.println("\n=== 프로젝트 상태 조회 결과 ===");
        System.out.println("프로젝트 ID: " + response.getProjectId());
        System.out.println("프로젝트 이름: " + response.getProjectName());

        System.out.println("\n=== 팀 목록 ===");
        for (TeamStatus team : response.getTeams()) {
            System.out.println("\n팀 ID: " + team.getTeamId());
            System.out.println("제출 상태: " + (team.isCompleted() ? "제출 완료" : "미제출"));
            System.out.println("팀원 목록:");
            for (TeamMemberInfo member : team.getMembers()) {
                System.out.println(" - " + member.getUserName() + "(" + member.getRole() + ")");
            }
        }

        System.out.println("\n=== 미편성 멤버 목록 ===");
        for (TeamMemberInfo member : response.getNoTeamMembers()) {
            System.out.println(" - " + member.getUserName());
        }
    }

    @Test
    @WithMockUser
    void createTeam_Success() throws Exception {
        // given
        TrackTeam request = new TrackTeam();
        request.setGenerationId(13);
        request.setTrackProjectId(2);

        List<TrackTeamMember> members = new ArrayList<>();
        TrackTeamMember leader = new TrackTeamMember();
        User leaderUser = new User();
        leaderUser.setUserId(8);
        leader.setTrackTeamId(request.getId());
        leader.setUser(leaderUser);
        leader.setRole(TeamRole.LEADER);

        TrackTeamMember member = new TrackTeamMember();
        User memberUser = new User();
        memberUser.setUserId(9);
        member.setTrackTeamId(request.getId());
        member.setTrackMemberId(9);
        member.setUser(memberUser);
        member.setRole(TeamRole.MEMBER);

        members.add(leader);
        members.add(member);
        request.setMembers(members);

        // when & then
        mockMvc.perform(post("/api/v1/project/track/2/team")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    @WithMockUser
    void createTeam_Fail_InvalidRequest() throws Exception {
        // given
        TrackTeam request = new TrackTeam();
        // 필수 값을 비워둠

        // when & then
        mockMvc.perform(post("/api/track/team")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }


    @Test
    @WithMockUser
    void deleteTeam_Success() throws Exception {
        // given
        int teamId = 5;  // DB에 존재하는 팀 ID

        // when & then
        mockMvc.perform(delete("/api/v1/project/track/2/team/{teamId}", teamId))
                .andExpect(status().isOk())
                .andDo(print());
    }


    @Test
    @WithMockUser
    void updateTeam_Success() throws Exception {
        // given
        int teamId = 6;

        // 팀 멤버 리스트 생성
        List<TrackTeamMember> members = new ArrayList<>();

        // 기존 멤버 1 (팀장)
        TrackTeamMember member1 = new TrackTeamMember();
        member1.setTrackTeamId(teamId);  // 팀 ID 설정
        User user1 = new User();
        user1.setUserId(8);  // 팀장의 userId 설정
        member1.setUser(user1);
        member1.setTrackMemberId(11);  // 멤버 ID 설정 (기존 ID)
        member1.setRole(TeamRole.LEADER);  // 역할 설정
        members.add(member1);

        // 새로운 멤버 2
        TrackTeamMember member2 = new TrackTeamMember();
        member2.setTrackTeamId(teamId);  // 팀 ID 설정
        User user2 = new User();
        user2.setUserId(10);  // 새로운 멤버의 userId 설정
        member2.setUser(user2);
        member2.setTrackMemberId(12);  // 멤버 ID 설정 (기존 ID)
        member2.setRole(TeamRole.MEMBER);  // 역할 설정
        members.add(member2);

        TrackTeam team = new TrackTeam();
        team.setMembers(members);
        // when & then
        mockMvc.perform(put("/api/v1/project/track/2/team/update")  // 팀 수정 API 호출
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(team)))  // 요청 본문에 TrackTeam JSON 데이터 첨부
                .andExpect(status().isOk())  // 상태 코드 200 OK 확인
                .andDo(print());  // 요청과 응답 출력
    }
}
