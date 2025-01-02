package com.honeyboard.api.web.recommend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.honeyboard.api.common.response.PageResponse;
import com.honeyboard.api.web.recommend.model.WebRecommend;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class WebRecommendControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void getAllWebRecommend_Success() throws Exception {
        // given
        int generationId = 13;
        int page = 1;
        int size = 16;

        // when & then
        MvcResult result = mockMvc.perform(get("/api/v1/web/recommend")
                        .param("generationId", String.valueOf(generationId))
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        PageResponse<WebRecommend> response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                objectMapper.getTypeFactory().constructParametricType(PageResponse.class, WebRecommend.class)
        );

        System.out.println("\n=== 웹 학습사이트 전체 조회 결과 ===");
        for (WebRecommend recommend : response.getContent()) {
            System.out.println("사이트 ID: " + recommend.getId());
            System.out.println("제목: " + recommend.getTitle());
            System.out.println("URL: " + recommend.getUrl());
            System.out.println("-------------------------");
        }
    }

    @Test
    @WithMockUser
    void searchWebRecommend_Success() throws Exception {
        // given
        int generationId = 13;
        String title = "Spring";
        int page = 1;
        int size = 16;

        // when & then
        mockMvc.perform(get("/api/v1/web/recommend/search")
                        .param("generationId", String.valueOf(generationId))
                        .param("title", title)
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockUser
    void getWebRecommend_Success() throws Exception {
        // given
        int recommendId = 1;

        // when & then
        mockMvc.perform(get("/api/v1/web/recommend/{recommendId}", recommendId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockUser
    void addWebRecommend_Success() throws Exception {
        // given
        WebRecommend request = new WebRecommend();
        request.setGenerationId(13);
        request.setTitle("Spring 학습 가이드");
        request.setUrl("https://spring.io/guides2");
        request.setContent("Spring 공식 가이드 문서입니다2.");
        request.setUserId(5);

        // when & then
        mockMvc.perform(post("/api/v1/web/recommend")
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
        WebRecommend request = new WebRecommend();
        request.setTitle("Updated Spring Guide");
        request.setUrl("https://spring.io/guides/updated");
        request.setContent("Updated Spring guide description");
        request.setUserId(5);
        request.setId(recommendId);

        // when & then
        mockMvc.perform(put("/api/v1/web/recommend/{recommendId}", recommendId)
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
        mockMvc.perform(delete("/api/v1/web/recommend/{recommendId}", recommendId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    // 실패 케이스 테스트
    @Test
    @WithMockUser
    void addWebRecommend_Fail_InvalidRequest() throws Exception {
        // given
        WebRecommend request = new WebRecommend();
        // 필수 값을 비워둠

        // when & then
        mockMvc.perform(post("/api/v1/web/recommend")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
}