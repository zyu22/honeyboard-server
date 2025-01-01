package com.honeyboard.api.schedule.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.honeyboard.api.schedule.model.Schedule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ScheduleControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "ADMIN")
    void createSchedule_Success() throws Exception {
        // given
        Schedule request = new Schedule();
        request.setContent("테스트 일정");

        // java.util.Date를 java.sql.Date로 변환
        request.setStartDate(LocalDate.parse("2024-12-03")); // LocalDate.parse()는 기본적으로 "yyyy-MM-dd" 형식을 지원
        request.setEndDate(LocalDate.parse("2024-12-04"));

        request.setScheduleType("NORMAL");
        request.setPublic(true);
        request.setUserId(5);
        request.setGenerationId(13);

        // when & then
        mockMvc.perform(post("/api/v1/schedule")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().string("일정이 등록되었습니다."))
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createSchedule_Fail_InvalidRequest() throws Exception {
        // given
        Schedule request = new Schedule();
        // 필수 필드 비움

        // when & then
        mockMvc.perform(post("/api/v1/schedule")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "test@test.com", roles = "ADMIN")
    void getSchedule_Success() throws Exception {
        // given
        int year = 2024;
        int month = 12;

        // when & then
        mockMvc.perform(get("/api/v1/schedule/{year}/{month}", year, month))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "test@test.com", roles = "ADMIN")
    void getSchedule_NoContent() throws Exception {
        // given
        int year = 2025;
        int month = 12;

        // when & then
        mockMvc.perform(get("/api/v1/schedule/{year}/{month}", year, month))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateSchedule_Success() throws Exception {
        // given
        int scheduleId = 1;
        Schedule request = new Schedule();
        request.setContent("수정된 일정");

        // java.util.Date를 java.sql.Date로 변환
        request.setStartDate(LocalDate.parse("2024-12-03")); // LocalDate.parse()는 기본적으로 "yyyy-MM-dd" 형식을 지원
        request.setEndDate(LocalDate.parse("2024-12-04"));

        request.setScheduleType("NORMAL");
        request.setPublic(true);

        // when & then
        mockMvc.perform(put("/api/v1/schedule/{scheduleId}", scheduleId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("일정이 수정되었습니다."))
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteSchedule_Success() throws Exception {
        // given
        int scheduleId = 1;

        // when & then
        mockMvc.perform(delete("/api/v1/schedule/{scheduleId}", scheduleId))
                .andExpect(status().isOk())
                .andExpect(content().string("일정을 삭제했습니다."))
                .andDo(print());
    }
}