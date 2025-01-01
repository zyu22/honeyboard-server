package com.honeyboard.api.project.track.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.honeyboard.api.project.track.model.TrackProject;
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
@AutoConfigureMockMvc(addFilters = false)
public class TrackProjectControllerTest {

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
        MvcResult result = mockMvc.perform(get("/api/v1/project/track")
                        .param("generation", String.valueOf(generationId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
    }

    @Test
    @WithMockUser
    void getTrackProject_Success() throws Exception {
        // given
        int trackId = 1;

        // when & then
        MvcResult result = mockMvc.perform(get("/api/v1/project/track/{trackId}", trackId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        TrackProject response = objectMapper.readValue(responseBody, TrackProject.class);

        System.out.println("\n=== 프로젝트 상세 조회 결과 ===");
        System.out.println("프로젝트 ID: " + response.getId());
        System.out.println("프로젝트 제목: " + response.getTitle());
    }

    @Test
    @WithMockUser
    void getTrackProjectMembers_Success() throws Exception {
        // given
        Integer generationId = 13;

        // when & then
        MvcResult result = mockMvc.perform(get("/api/v1/project/track/member")
                        .param("generation", String.valueOf(generationId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
    }

    @Test
    @WithMockUser
    void createTrackProject_Success() throws Exception {
        // given
        TrackProject request = new TrackProject();
        request.setGenerationId(13);
        request.setTitle("테스트 프로젝트");

        List<Integer> excludedMemberIds = new ArrayList<>();
        excludedMemberIds.add(1);
        excludedMemberIds.add(2);
        request.setExcludedMemberIds(excludedMemberIds);

        // when & then
        mockMvc.perform(post("/api/v1/project/track")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    @WithMockUser
    void updateTrackProject_Success() throws Exception {
        // given
        int trackId = 1;
        TrackProject request = new TrackProject();
        request.setGenerationId(13);
        request.setTitle("수정된 프로젝트");

        // when & then
        mockMvc.perform(put("/api/v1/project/track/{trackId}", trackId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockUser
    void deleteTrackProject_Success() throws Exception {
        // given
        int trackId = 1;

        // when & then
        mockMvc.perform(delete("/api/v1/project/track/{trackId}", trackId))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockUser
    void createTrackProject_Fail_InvalidRequest() throws Exception {
        // given
        TrackProject request = new TrackProject();
        // 필수 값을 비워둠

        // when & then
        mockMvc.perform(post("/api/v1/project/track")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
}