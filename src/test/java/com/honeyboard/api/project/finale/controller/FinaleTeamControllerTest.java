package com.honeyboard.api.project.finale.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class FinaleTeamControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void getAllSubmittedStatus_Success() throws Exception {
        // given
        int projectId = 1;
        String date = "20240102";

        // when & then
        MvcResult result = mockMvc.perform(get("/api/v1/project/finale/{projectId}/status", projectId)
                        .param("date", date)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
    }

    @Test
    @WithMockUser
    void getRemainedUsers_Success() throws Exception {
        // given
        Integer generationId = 13;

        // when & then
        MvcResult result = mockMvc.perform(get("/api/v1/project/finale/team/remaining")
                        .param("generation", String.valueOf(generationId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
    }

    @Test
    @WithMockUser
    void getAllFinaleProjects_Success() throws Exception {
        // given
        Integer generationId = 13;

        // when & then
        MvcResult result = mockMvc.perform(get("/api/v1/project/finale")
                        .param("generation", String.valueOf(generationId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
    }
}
