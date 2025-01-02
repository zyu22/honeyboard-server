package com.honeyboard.api.admin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.honeyboard.api.generation.model.Generation;
import com.honeyboard.api.user.model.UserInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)  // 시큐리티 필터 비활성화
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void getUsersByGeneration_Success() throws Exception {
        // given
        int generationId = 13; // 테스트용 기수 ID

        // when
        MvcResult result = mockMvc.perform(get("/api/v1/admin/user")
                        .param("generationId", String.valueOf(generationId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        // then
        String responseBody = result.getResponse().getContentAsString();
        List<UserInfo> users = objectMapper.readValue(responseBody,
                objectMapper.getTypeFactory().constructCollectionType(List.class, UserInfo.class));

        System.out.println("\n=== 기수별 학생 조회 결과 ===");
        System.out.println("기수 ID: " + generationId);
        System.out.println("학생 수: " + users.size());
        for (UserInfo user : users) {
            System.out.println("------------------------");
            System.out.println("이름: " + user.getName());
            System.out.println("이메일: " + user.getEmail());
        }
    }

    @Test
    @WithMockUser
    void getUserByUserId_Success() throws Exception {
        // given
        int userId = 9; // 테스트용 유저 ID

        // when
        MvcResult result = mockMvc.perform(get("/api/v1/admin/user/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        // then
        String responseBody = result.getResponse().getContentAsString();
        UserInfo user = objectMapper.readValue(responseBody, UserInfo.class);

        System.out.println("\n=== 유저 상세정보 조회 결과 ===");
        System.out.println("유저 ID: " + userId);
        System.out.println("이름: " + user.getName());
        System.out.println("이메일: " + user.getEmail());
    }

    @Test
    @WithMockUser
    void updateUser_Success() throws Exception {
        // given
        int userId = 100; // 테스트용 유저 ID
        UserInfo updatedUserInfo = new UserInfo();
        updatedUserInfo.setName("Updated Name");
        updatedUserInfo.setEmail("updated_email@example.com");

        // when
        mockMvc.perform(put("/api/v1/admin/user/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUserInfo)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockUser
    void createGeneration_Success() throws Exception {
        // given
        Generation generation = new Generation();
        generation.setName("New Generation");

        // when
        mockMvc.perform(post("/api/v1/admin/generation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(generation)))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    @WithMockUser
    void updateGenerationIsActive_Success() throws Exception {
        // given
        int generationId = 1; // 테스트용 기수 ID

        // when
        mockMvc.perform(patch("/api/v1/admin/generation/{generationId}isActive/", generationId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockUser
    void deleteGeneration_Success() throws Exception {
        // given
        int generationId = 1; // 테스트용 기수 ID

        // when
        mockMvc.perform(delete("/api/v1/admin/generation/{generationId}", generationId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
