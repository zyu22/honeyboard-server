package com.honeyboard.api.project.finale.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.honeyboard.api.project.finale.model.request.FinaleProjectBoardRequest;
import com.honeyboard.api.project.finale.model.request.FinaleProjectCreate;
import com.honeyboard.api.project.finale.model.request.FinaleProjectUpdate;
import com.honeyboard.api.project.finale.model.response.FinaleProjectBoardDetail;
import com.honeyboard.api.project.finale.model.response.FinaleProjectDetail;
import com.honeyboard.api.project.finale.model.response.FinaleProjectResponse;
import com.honeyboard.api.project.finale.service.FinaleProjectBoardService;
import com.honeyboard.api.project.finale.service.FinaleProjectService;
import com.honeyboard.api.project.finale.service.FinaleTeamService;
import com.honeyboard.api.user.model.User;
import com.honeyboard.api.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FinaleProjectController.class)
@AutoConfigureMockMvc(addFilters = false)
@WithMockUser(username = "testUser", roles = "USER")
class FinaleProjectControllerTest3 {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private FinaleProjectService finaleProjectService;
    private FinaleProjectBoardService finaleProjectBoardService;
    private
    FinaleTeamService finaleTeamService;
    private UserService userService;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public FinaleProjectService finaleProjectService() {
            return mock(FinaleProjectService.class);
        }

        @Bean
        public UserService userService() {
            return mock(UserService.class);
        }
    }

    @BeforeEach
    void setUp() {
        finaleProjectService = mock(FinaleProjectService.class);
        userService = mock(UserService.class);
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    @DisplayName("피날레 프로젝트 전체 조회 성공")
    void getFinaleProjectResponse_Success() throws Exception {
        // given
        int generationId = 13;
        FinaleProjectResponse response = new FinaleProjectResponse();
        given(userService.getActiveGenerationId()).willReturn(generationId);
        given(finaleProjectService.getFinaleResponse(generationId)).willReturn(response);

        // when & then
        mockMvc.perform(get("/api/v1/project/finale")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    @DisplayName("피날레 프로젝트 전체 조회 - 데이터 없음")
    void getFinaleProjectResponse_NoContent() throws Exception {
        // given
        int generationId = 13;
        given(userService.getActiveGenerationId()).willReturn(generationId);
        given(finaleProjectService.getFinaleResponse(generationId)).willReturn(null);

        // when & then
        mockMvc.perform(get("/api/v1/project/finale")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());
    }


    @Test
    @WithMockUser(username = "user", roles = "USER")
    @DisplayName("피날레 프로젝트 상세 조회 성공")
    void getFinaleProjectDetail_Success() throws Exception {
        // given
        int projectId = 1;
        FinaleProjectDetail response = new FinaleProjectDetail();
        given(finaleProjectService.getFinaleProjectDetail(projectId)).willReturn(response);

        // when & then
        mockMvc.perform(get("/api/v1/project/finale/{finaleProjectId}", projectId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    @DisplayName("피날레 게시글 상세 조회 성공")
    void getFinaleProjectBoardDetail_Success() throws Exception {
        // given
        int projectId = 1;
        int boardId = 1;
        FinaleProjectBoardDetail response = new FinaleProjectBoardDetail();
        given(finaleProjectBoardService.getFinaleProjectBoardDetail(projectId, boardId)).willReturn(response);

        // when & then
        mockMvc.perform(get("/api/v1/project/finale/{finaleProjectId}/board/{boardId}", projectId, boardId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    @DisplayName("피날레 프로젝트 생성 성공")
    void createFinaleProject_Success() throws Exception {
        // given
        FinaleProjectCreate request = new FinaleProjectCreate();
        User user = new User();
        given(finaleProjectService.createFinaleProject(any(), user.getGenerationId(), user.getUserId())).willReturn(1);

        // when & then
        mockMvc.perform(post("/api/v1/project/finale")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    @DisplayName("피날레 게시글 작성 성공")
    void createFinaleProjectBoard_Success() throws Exception {
        // given
        int projectId = 1;
        FinaleProjectBoardRequest request = new FinaleProjectBoardRequest();
        User user = new User();
        given(finaleProjectBoardService.createFinaleProjectBoard(eq(projectId), any(), any())).willReturn(1);

        // when & then
        mockMvc.perform(post("/api/v1/project/finale/{finaleProjectId}/board", projectId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    @DisplayName("피날레 프로젝트 수정 성공")
    void updateFinaleProject_Success() throws Exception {
        // given
        int projectId = 1;
        FinaleProjectUpdate request = new FinaleProjectUpdate();

        // when & then
        mockMvc.perform(put("/api/v1/project/finale/{finaleProjectId}", projectId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    @DisplayName("피날레 프로젝트 삭제 성공")
    void deleteFinaleProject_Success() throws Exception {
        // given
        int projectId = 1;

        // when & then
        mockMvc.perform(delete("/api/v1/project/finale/team/{finaleProjectId}", projectId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

}
