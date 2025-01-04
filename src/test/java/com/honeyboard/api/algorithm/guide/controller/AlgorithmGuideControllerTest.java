package com.honeyboard.api.algorithm.guide.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.honeyboard.api.algorithm.guide.model.AlgorithmGuide;
import com.honeyboard.api.common.response.PageResponse;
import com.fasterxml.jackson.core.type.TypeReference;  // 추가된 import
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;  // 추가된 import
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;  // 추가된 import

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)  // 시큐리티 필터 비활성화
public class AlgorithmGuideControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;  // JSON 변환을 위한 매퍼

    @Test
    @WithMockUser  // 인증된 사용자의 테스트
    void getAllAlgorithmGuide() throws Exception {
        // given
        int currentPage = 1;
        int pageSize = 8;
        int generationId = 13; // 기본값
        String title = "";  // 검색어 예시

        // when
        MvcResult result = mockMvc.perform(get("/api/v1/algorithm/guide")  // 엔드포인트 URL
                        .param("currentPage", String.valueOf(currentPage))
                        .param("pageSize", String.valueOf(pageSize))
                        .param("generationId", String.valueOf(generationId))
                        .param("title", title)  // 검색어
                        .contentType(MediaType.APPLICATION_JSON))  // 응답 형식
                .andExpect(status().isOk())  // HTTP 200 OK 응답 기대
                .andDo(print())  // 응답 출력
                .andReturn();

        // then
        String responseBody = result.getResponse().getContentAsString();
        PageResponse<AlgorithmGuide> response = objectMapper.readValue(responseBody,
                new TypeReference<PageResponse<AlgorithmGuide>>() {});  // 제네릭 타입을 사용하여 응답 파싱

        // 페이지 응답 검증
        assertNotNull(response);
        assertTrue(response.getContent().size() > 0, "알고리즘 가이드 목록이 비어있지 않아야 합니다.");
        System.out.println("=== 알고리즘 가이드 목록 ===");
        for (AlgorithmGuide guide : response.getContent()) {
            System.out.println("ID: " + guide.getId());
            System.out.println("Title: " + guide.getTitle());
            System.out.println("Thumbnail: " + guide.getThumbnail());
            System.out.println("Created At: " + guide.getCreatedAt());
            System.out.println("--------------------------------");
        }
    }
}
