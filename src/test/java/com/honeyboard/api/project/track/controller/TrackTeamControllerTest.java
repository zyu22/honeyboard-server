package com.honeyboard.api.project.track.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.honeyboard.api.project.model.TeamRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Transactional
class TrackTeamControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void createTrackTeam_Success() throws Exception {
        // given
        int trackProjectId = 2;
        TeamRequest teamRequest = new TeamRequest();
        teamRequest.setLeaderId(18); // 예시 사용자 ID
        teamRequest.setMemberIds(Arrays.asList(19, 20)); // 팀원 ID 리스트

        // when & then
        mockMvc.perform(post("/api/v1/project/track/{trackProjectId}/team", trackProjectId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(teamRequest)))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    @WithMockUser
    void updateTrackTeam_Success() throws Exception {
        // given
        int trackProjectId = 2;
        int trackTeamId = 6; // 기존에 존재하는 팀 ID
        TeamRequest teamRequest = new TeamRequest();
        teamRequest.setLeaderId(5); // 수정할 리더 ID
        teamRequest.setMemberIds(Arrays.asList(23, 24)); // 수정된 팀원 ID 리스트

        // when & then
        mockMvc.perform(put("/api/v1/project/track/{trackProjectId}/team/{trackTeamId}", trackProjectId, trackTeamId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(teamRequest)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockUser
    void createTrackTeam_Fail_InvalidRequest() throws Exception {
        // given
        int trackProjectId = 2;
        TeamRequest teamRequest = new TeamRequest();
        // 필수 값을 비워둠 (leaderId와 memberIds가 null)

        // when & then
        mockMvc.perform(post("/api/v1/project/track/{trackProjectId}/team", trackProjectId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(teamRequest)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
}