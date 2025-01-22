package com.honeyboard.api.project.track.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.honeyboard.api.project.track.model.request.TrackProjectRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Transactional
class TrackProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void getAllTrackProjects_Success() throws Exception {
        // given
        Integer generationId = 13;

        // when & then
        mockMvc.perform(get("/api/v1/project/track")
                        .param("generationId", String.valueOf(generationId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andDo(print());
    }

    @Test
    @WithMockUser
    void getTrackProject_Success() throws Exception {
        // given
        int trackProjectId = 2;

        // when & then
        mockMvc.perform(get("/api/v1/project/track/{trackProjectId}", trackProjectId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andDo(print());
    }

    @Test
    @WithMockUser
    void getTrackProjectMembers_Success() throws Exception {
        // given
        int trackProjectId = 2;

        // when & then
        mockMvc.perform(get("/api/v1/project/track/{trackProjectId}/available-user", trackProjectId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andDo(print());
    }

    @Test
    @WithMockUser
    void createTrackProject_Success() throws Exception {
        // given
        TrackProjectRequest request = new TrackProjectRequest();
        request.setTitle("테스트 프로젝트");
        request.setDescription("테스트 입니다!!!!");
        request.setObjective("이건 테스트 프로젝트입니다.");

        List<Integer> excludedMemberIds = new ArrayList<>();
        excludedMemberIds.add(10);
        excludedMemberIds.add(15);
        request.setExcludedMembers(excludedMemberIds);

        // when & then
        mockMvc.perform(post("/api/v1/project/track")
                        .param("userId", "5")  // userId 파라미터 추가
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andDo(print());
    }

    @Test
    @WithMockUser
    void updateTrackProject_Success() throws Exception {
        // given
        int trackProjectId = 2;
        TrackProjectRequest request = new TrackProjectRequest();
        request.setTitle("수정된 프로젝트");
        request.setObjective("수정되었는데요?");
        request.setDescription("수정수정수정데스네");

        // when & then
        mockMvc.perform(put("/api/v1/project/track/{trackProjectId}", trackProjectId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockUser
    void deleteTrackProject_Success() throws Exception {
        // given
        int trackProjectId = 2;

        // when & then
        mockMvc.perform(delete("/api/v1/project/track/{trackProjectId}", trackProjectId))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockUser
    void createTrackProject_Fail_InvalidRequest() throws Exception {
        // given
        TrackProjectRequest request = new TrackProjectRequest();
        // 필수 값을 비워둠

        // when & then
        mockMvc.perform(post("/api/v1/project/track")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
}