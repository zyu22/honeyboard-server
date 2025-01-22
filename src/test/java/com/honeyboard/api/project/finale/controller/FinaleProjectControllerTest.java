package com.honeyboard.api.project.finale.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.honeyboard.api.project.finale.model.request.FinaleProjectCreate;
import com.honeyboard.api.project.finale.model.request.FinaleProjectUpdate;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Transactional
class FinaleProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void getFinaleResponse_Success() throws Exception {
        // given
        Integer generationId = 13;

        // when & then
        mockMvc.perform(get("/api/v1/project/finale")
                        .param("generationId", String.valueOf(generationId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.projects[0].id").exists())
                .andExpect(jsonPath("$.projects[0].title").exists())
                .andExpect(jsonPath("$.projects[0].members").isArray())
                .andExpect(jsonPath("$.noTeamUsers").isArray())
                .andExpect(jsonPath("$.teams").isArray())
                .andDo(print());
    }

    @Test
    @WithMockUser
    void getFinaleProjectDetail_Success() throws Exception {
        // given
        int finaleProjectId = 2;

        // when & then
        mockMvc.perform(get("/api/v1/project/finale/{finaleProjectId}", finaleProjectId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").exists())
                .andExpect(jsonPath("$.description").exists())
                .andExpect(jsonPath("$.url").exists())
                .andExpect(jsonPath("$.members").isArray())
                .andExpect(jsonPath("$.boards").isArray())
                .andDo(print());
    }

    @Test
    @WithMockUser
    void createFinaleProject_Success() throws Exception {
        // given
        FinaleProjectCreate request = new FinaleProjectCreate();
        request.setTitle("테스트 프로젝트");
        request.setDescription("테스트 설명입니다");
        request.setUrl("http://test.com");

        TeamRequest teamRequest = new TeamRequest();
        teamRequest.setLeaderId(1);
        teamRequest.setMemberIds(Arrays.asList(2, 3, 4));
        request.setTeams(teamRequest);

        // when & then
        mockMvc.perform(post("/api/v1/project/finale")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    @WithMockUser
    void updateFinaleProject_Success() throws Exception {
        // given
        int finaleProjectId = 2;
        FinaleProjectUpdate request = new FinaleProjectUpdate();
        request.setTitle("수정된 프로젝트");
        request.setDescription("수정된 설명");
        request.setUrl("http://updated-test.com");

        // when & then
        mockMvc.perform(put("/api/v1/project/finale/{finaleProjectId}", finaleProjectId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockUser
    void deleteFinaleProject_Success() throws Exception {
        // given
        int finaleProjectId = 2;

        // when & then
        mockMvc.perform(delete("/api/v1/project/finale/{finaleProjectId}", finaleProjectId))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockUser
    void createFinaleProject_Fail_InvalidRequest() throws Exception {
        // given
        FinaleProjectCreate request = new FinaleProjectCreate();
        // 필수 값을 비워둠

        // when & then
        mockMvc.perform(post("/api/v1/project/finale")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @WithMockUser
    void createFinaleProject_Fail_InvalidTeamRequest() throws Exception {
        // given
        FinaleProjectCreate request = new FinaleProjectCreate();
        request.setTitle("테스트 프로젝트");
        request.setDescription("테스트 설명입니다");
        request.setUrl("http://test.com");

        TeamRequest teamRequest = new TeamRequest();
        teamRequest.setLeaderId(0); // 잘못된 리더 ID
        request.setTeams(teamRequest);

        // when & then
        mockMvc.perform(post("/api/v1/project/finale")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @WithMockUser
    void updateFinaleProject_Fail_ProjectNotFound() throws Exception {
        // given
        int nonExistentProjectId = 9999;
        FinaleProjectUpdate request = new FinaleProjectUpdate();
        request.setTitle("수정된 프로젝트");
        request.setDescription("수정된 설명");
        request.setUrl("http://updated-test.com");

        // when & then
        mockMvc.perform(put("/api/v1/project/finale/{finaleProjectId}", nonExistentProjectId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
}