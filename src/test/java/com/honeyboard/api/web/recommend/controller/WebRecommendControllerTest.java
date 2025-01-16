package com.honeyboard.api.web.recommend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.honeyboard.api.common.response.PageResponse;
import com.honeyboard.api.web.recommend.model.request.WebRecommendRequest;
import com.honeyboard.api.web.recommend.model.response.WebRecommendDetail;
import com.honeyboard.api.web.recommend.model.response.WebRecommendList;
import com.honeyboard.api.web.recommend.service.WebRecommendService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Transactional
class WebRecommendControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void getAllWebRecommend_Success() throws Exception {
        mockMvc.perform(get("/api/v1/web/recommend")
                        .param("currentPage", "1")
                        .param("pageSize", "16"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockUser
    void getAllWebRecommend_WithTitle_Success() throws Exception {
        mockMvc.perform(get("/api/v1/web/recommend")
                        .param("currentPage", "1")
                        .param("pageSize", "16")
                        .param("title", "테스트"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockUser
    void getWebRecommend_Success() throws Exception {
        mockMvc.perform(get("/api/v1/web/recommend/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andDo(print());
    }

    @Test
    @WithMockUser
    void addWebRecommend_Success() throws Exception {
        // given
        WebRecommendRequest request = new WebRecommendRequest();
        request.setTitle("테스트 웹 추천");
        request.setContent("테스트 웹 추천 내용");
        request.setUrl("https://example.com345");


        // when & then
        mockMvc.perform(post("/api/v1/web/recommend")
                        .param("userId", String.valueOf(9))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    @WithMockUser
    void updateWebRecommend_Success() throws Exception {
        // given
        int recommendId = 1;
        WebRecommendRequest request = new WebRecommendRequest();
        request.setTitle("수정된 웹 추천");
        request.setContent("수정된 웹 추천 내용");
        request.setUrl("https://updated-example.sdf");

        // when & then
        mockMvc.perform(put("/api/v1/web/recommend/{recommendId}", recommendId)
                        .param("userId", String.valueOf(5))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockUser
    void deleteWebRecommend_Success() throws Exception {
        // given
        int recommendId = 1;

        // when & then
        mockMvc.perform(delete("/api/v1/web/recommend/{recommendId}", recommendId))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockUser
    void addWebRecommend_Fail_InvalidRequest() throws Exception {
        // given
        WebRecommendRequest request = new WebRecommendRequest();
        // 필수 값을 비워둠

        // when & then
        mockMvc.perform(post("/api/v1/web/recommend")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
}