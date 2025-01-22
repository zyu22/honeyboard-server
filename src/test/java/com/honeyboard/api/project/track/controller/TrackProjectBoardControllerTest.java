package com.honeyboard.api.project.track.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.honeyboard.api.project.track.model.request.TrackProjectBoardRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Transactional
class TrackProjectBoardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void getTrackBoard_Success() throws Exception {
        // given
        int trackProjectId = 2;
        int trackTeamId = 6;
        int boardId = 1;

        // when & then
        mockMvc.perform(get("/api/v1/project/track/{trackProjectId}/track/{trackTeamId}/board/{boardId}",
                        trackProjectId, trackTeamId, boardId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andDo(print());
    }

    @Test
    @WithMockUser
    void createTrackProjectBoard_Success() throws Exception {
        // given
        int trackProjectId = 2;
        int trackTeamId = 12;
        int userId = 5;
        TrackProjectBoardRequest request = new TrackProjectBoardRequest();
        request.setTitle("테스트 게시글");
        request.setContent("테스트 게시글 내용입니다.");
        request.setUrl("www.naver.com");
        request.setThumbnail("aaa");

        // when & then
        mockMvc.perform(post("/api/v1/project/track/{trackProjectId}/team/{trackTeamId}/board",
                        trackProjectId, trackTeamId)
                        .param("userId", String.valueOf(userId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andDo(print());
    }

    @Test
    @WithMockUser
    void updateTrackProjectBoard_Success() throws Exception {
        // given
        int trackProjectId = 2;
        int trackTeamId = 6;
        int boardId = 1;
        TrackProjectBoardRequest request = new TrackProjectBoardRequest();
        request.setTitle("수정된 게시글");
        request.setContent("수정된 게시글 내용입니다.");
        request.setUrl("adf");
        request.setThumbnail("aaa");


        // when & then
        mockMvc.perform(put("/api/v1/project/track/{trackProjectId}/team/{trackTeamId}/board/{boardId}",
                        trackProjectId, trackTeamId, boardId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockUser
    void deleteTrackProjectBoard_Success() throws Exception {
        // given
        int trackProjectId = 2;
        int boardId = 1;

        // when & then
        mockMvc.perform(delete("/api/v1/project/track/{trackProjectId}/board/{boardId}",
                        trackProjectId, boardId))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockUser
    void createTrackProjectBoard_Fail_InvalidRequest() throws Exception {
        // given
        int trackProjectId = 2;
        int trackTeamId = 1;
        TrackProjectBoardRequest request = new TrackProjectBoardRequest();
        // 필수 값을 비워둠

        // when & then
        mockMvc.perform(post("/api/v1/project/track/{trackProjectId}/team/{trackTeamId}/board",
                        trackProjectId, trackTeamId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
}