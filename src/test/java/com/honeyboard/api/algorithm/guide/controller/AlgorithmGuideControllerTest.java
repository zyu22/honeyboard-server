package com.honeyboard.api.algorithm.guide.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.honeyboard.api.algorithm.guide.model.AlgorithmGuide;
import com.honeyboard.api.algorithm.guide.service.AlgorithmGuideService;
import com.honeyboard.api.user.model.CustomUserDetails;
import com.honeyboard.api.user.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AlgorithmGuideControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AlgorithmGuideService algorithmGuideService;

    @Test
    @WithMockUser
    void getAllAlgorithmGuide_Success() throws Exception {
        // given
        AlgorithmGuide guide = new AlgorithmGuide();
        guide.setId(1);
        guide.setTitle("Algorithm Guide Test");

        when(algorithmGuideService.getAlgorithmGuides(anyInt()))
            .thenReturn(Arrays.asList(guide));

        // when & then
        mockMvc.perform(get("/api/v1/algorithm/guide")
                .param("generationId", "13"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockUser
    void getAllAlgorithmGuide_NoContent() throws Exception {
        // given
        when(algorithmGuideService.getAlgorithmGuides(anyInt()))
            .thenReturn(Collections.emptyList());

        // when & then
        mockMvc.perform(get("/api/v1/algorithm/guide")
                .param("generationId", "13"))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    @WithMockUser
    void getAlgorithmGuideDetail_Success() throws Exception {
        // given
        AlgorithmGuide guide = new AlgorithmGuide();
        guide.setId(1);
        guide.setTitle("Algorithm Guide Detail Test");

        when(algorithmGuideService.getAlgorithmGuideDetail(anyInt(), anyBoolean()))
            .thenReturn(guide);

        // when & then
        mockMvc.perform(get("/api/v1/algorithm/guide/1")
                .param("bookmark", "false"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockUser
    void addAlgorithmGuide_Success() throws Exception {
        // given
        AlgorithmGuide guide = new AlgorithmGuide();
        guide.setTitle("New Algorithm Guide");
        guide.setContent("Test Content");

        when(algorithmGuideService.addAlgorithmGuide(anyInt(), any(AlgorithmGuide.class)))
            .thenReturn(true);

        // when & then
        mockMvc.perform(post("/api/v1/algorithm/guide")
                .param("generationId", "13")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(guide)))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    @WithMockUser
    void updateAlgorithmGuide_Success() throws Exception {
        // given
        AlgorithmGuide guide = new AlgorithmGuide();
        guide.setTitle("Updated Algorithm Guide");
        guide.setContent("Updated Content");

        when(algorithmGuideService.updateAlgorithmGuide(anyInt(), any(AlgorithmGuide.class)))
            .thenReturn(true);

        // when & then
        mockMvc.perform(put("/api/v1/algorithm/guide/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(guide)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockUser
    void updateAlgorithmGuide_NotFound() throws Exception {
        // given
        AlgorithmGuide guide = new AlgorithmGuide();
        guide.setTitle("Updated Algorithm Guide");

        when(algorithmGuideService.updateAlgorithmGuide(anyInt(), any(AlgorithmGuide.class)))
            .thenReturn(false);

        // when & then
        mockMvc.perform(put("/api/v1/algorithm/guide/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(guide)))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @WithMockUser
    void deleteAlgorithmGuide_Success() throws Exception {
        // given
        when(algorithmGuideService.deleteAlgorithmGuide(anyInt()))
            .thenReturn(true);

        // when & then
        mockMvc.perform(delete("/api/v1/algorithm/guide/1"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockUser
    void deleteAlgorithmGuide_NotFound() throws Exception {
        // given
        when(algorithmGuideService.deleteAlgorithmGuide(anyInt()))
            .thenReturn(false);

        // when & then
        mockMvc.perform(delete("/api/v1/algorithm/guide/999"))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
}