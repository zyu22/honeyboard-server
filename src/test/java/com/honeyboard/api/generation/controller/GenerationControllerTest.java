package com.honeyboard.api.generation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.honeyboard.api.generation.model.Generation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)  // 시큐리티 필터 비활성화
public class GenerationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void getAllGenerations_Success() throws Exception {
        // when
        MvcResult result = mockMvc.perform(get("/api/v1/generation")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        // then
        String responseBody = result.getResponse().getContentAsString();
        List<Generation> generations = objectMapper.readValue(responseBody,
                objectMapper.getTypeFactory().constructCollectionType(List.class, Generation.class));

        System.out.println("\n=== 전체 기수 조회 결과 ===");
        for (Generation generation : generations) {
            System.out.println("기수 ID: " + generation.getId());
            System.out.println("기수 이름: " + generation.getName());
            System.out.println("활성화 여부: " + generation.isActive());
            System.out.println("------------------------");
        }
    }

    @Test
    @WithMockUser
    void getAllGenerations_NoContent() throws Exception {
        // 데이터가 없는 경우 테스트
        mockMvc.perform(get("/api/v1/generation")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    @WithMockUser
    void getGenerationById_NoContent() throws Exception {
        // given
        String nonExistingGenerationId = "999";  // 존재하지 않는 기수 ID

        // when & then
        mockMvc.perform(get("/api/v1/generation/{generationId}", nonExistingGenerationId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());
    }
}