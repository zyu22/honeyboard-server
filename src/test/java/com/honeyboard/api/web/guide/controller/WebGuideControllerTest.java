package com.honeyboard.api.web.guide.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.honeyboard.api.common.response.PageResponse;
import com.honeyboard.api.web.guide.model.WebGuide;
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
public class WebGuideControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void getAllWebGuide_Success() throws Exception {
        // given
        int generationId = 13;
        int currentPage = 1;
        int pageSize = 8;

        // when & then
        MvcResult result = mockMvc.perform(get("/api/v1/web/guide")
                        .param("generationId", String.valueOf(generationId))
                        .param("currentPage", String.valueOf(currentPage))
                        .param("pageSize", String.valueOf(pageSize))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        PageResponse<WebGuide> response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                objectMapper.getTypeFactory().constructParametricType(PageResponse.class, WebGuide.class)
        );

        System.out.println("\n=== 웹 개념 전체 조회 결과 ===");
        for (WebGuide guide : response.getContent()) {
            System.out.println("개념 ID: " + guide.getId());
            System.out.println("제목: " + guide.getTitle());
            System.out.println("-------------------------");
        }
    }

    @Test
    @WithMockUser
    void searchWebGuide_Success() throws Exception {
        // given
        int generationId = 13;
        String title = "프론트엔드";
        int currentPage = 1;
        int pageSize = 8;

        // when & then
        mockMvc.perform(get("/api/v1/web/guide/search")
                        .param("generationId", String.valueOf(generationId))
                        .param("title", title)
                        .param("currentPage", String.valueOf(currentPage))
                        .param("pageSize", String.valueOf(pageSize))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockUser
    void getWebGuide_Success() throws Exception {
        // given
        int guideId = 4;

        // when & then
        mockMvc.perform(get("/api/v1/web/guide/{guideId}", guideId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockUser
    void addWebGuide_Success() throws Exception {
        // given
        WebGuide request = new WebGuide();
        request.setUserId(5);
        request.setGenerationId(13);
        request.setTitle("HTML 기초");
        request.setContent("HTML은 웹의 기초가 되는 마크업 언어입니다.");

        // when & then
        mockMvc.perform(post("/api/v1/web/guide")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    @WithMockUser
    void updateWebGuide_Success() throws Exception {
        // given
        int guideId = 4;
        WebGuide request = new WebGuide();
        request.setUserId(5);
        request.setTitle("Updated HTML Guide");
        request.setContent("Updated HTML content");

        // when & then
        mockMvc.perform(put("/api/v1/web/guide/{guideId}", guideId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockUser
    void deleteWebGuide_Success() throws Exception {
        // given
        int guideId = 4;

        // when & then
        mockMvc.perform(delete("/api/v1/web/guide/{guideId}", guideId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockUser
    void addWebGuide_Fail_InvalidRequest() throws Exception {
        // given
        WebGuide request = new WebGuide();
        // 필수 값을 비워둠

        // when & then
        mockMvc.perform(post("/api/v1/web/guide")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
}