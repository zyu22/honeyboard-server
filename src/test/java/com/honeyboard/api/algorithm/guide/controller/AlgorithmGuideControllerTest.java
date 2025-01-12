package com.honeyboard.api.algorithm.guide.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.honeyboard.api.algorithm.guide.model.request.AlgorithmGuideRequest;
import com.honeyboard.api.config.TestConfig;
import com.honeyboard.api.jwt.model.service.JwtService;
import com.honeyboard.api.user.model.User;
import com.honeyboard.api.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "mybatis.type-aliases-package=com.honeyboard.api.algorithm.guide.model,com.honeyboard.api.user.model"
})
class AlgorithmGuideControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    private String testToken;
    private User testUser;
    private AlgorithmGuideRequest testRequest;

    @BeforeEach
    void setUp() {
        // 테스트 유저는 실제 DB에서 조회
        testUser = userService.getUser(1); // 실제 존재하는 유저 ID로 변경 필요

        // JWT 액세스 토큰 생성
        testToken = jwtService.generateAccessToken(testUser);

        // 테스트 요청 객체 설정
        testRequest = new AlgorithmGuideRequest();
        testRequest.setTitle("Test Algorithm");
        testRequest.setContent("Test Content");
    }

    @Test
    @DisplayName("알고리즘 가이드 목록 조회 테스트")
    void getAllAlgorithmGuideTest() throws Exception {
        mockMvc.perform(get("/api/v1/algorithm/guide")
                        .param("currentPage", "1")
                        .param("pageSize", "8")
                        .param("generationId", String.valueOf(testUser.getGenerationId())))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("알고리즘 가이드 상세 조회 테스트")
    void getAlgorithmGuideDetailTest() throws Exception {
        mockMvc.perform(get("/api/v1/algorithm/guide/1")
                        .header("Authorization", "Bearer " + testToken))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("알고리즘 가이드 작성 테스트")
    void addAlgorithmGuideTest() throws Exception {
        mockMvc.perform(post("/api/v1/algorithm/guide")
                        .header("Authorization", "Bearer " + testToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    @DisplayName("알고리즘 가이드 수정 테스트")
    void updateAlgorithmGuideTest() throws Exception {
        mockMvc.perform(put("/api/v1/algorithm/guide/1")
                        .header("Authorization", "Bearer " + testToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testRequest)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("알고리즘 가이드 삭제 테스트")
    void deleteAlgorithmGuideTest() throws Exception {
        mockMvc.perform(delete("/api/v1/algorithm/guide/1")
                        .header("Authorization", "Bearer " + testToken))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("인증없이 접근시 실패 테스트")
    void unauthorizedAccessTest() throws Exception {
        mockMvc.perform(get("/api/v1/algorithm/guide/1"))
                .andExpect(status().isUnauthorized());
    }
}