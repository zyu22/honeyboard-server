//package com.honeyboard.api.algorithm.solution.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.honeyboard.api.algorithm.solution.model.AlgorithmSolution;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//
//public class SolutionControllerTest {
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Test
//    @WithMockUser
//    void createAlgorithmSolution_Success() throws Exception {
//        // given
//        AlgorithmSolution request = new AlgorithmSolution();
//        request.setProblemId(1);
//        request.setTitle("테스트 풀이");
//        request.setSummary("테스트 요약");
//        request.setContent("테스트 내용");
//        request.setUserId(1);
//        request.setRuntime(100);
//        request.setMemory(256);
//        request.setLanguageId(1);
//        request.setGenerationId(13);
//
//        // when & then
//        mockMvc.perform(post("/api/v1/algorithm/problem/1/solution")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isCreated())
//                .andDo(print());
//    }
//
//    @Test
//    @WithMockUser
//    void createAlgorithmSolution_Fail_InvalidRequest() throws Exception {
//        // given
//        AlgorithmSolution request = new AlgorithmSolution();
//        // 필수 값을 비워둠
//
//        // when & then
//        mockMvc.perform(post("/api/v1/algorithm/problem/1/solution")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isBadRequest())
//                .andDo(print());
//    }
//
//    @Test
//    @WithMockUser
//    void getAllAlgorithmSolution_Success() throws Exception {
//        // when & then
//        mockMvc.perform(get("/api/v1/algorithm/problem/1/solution")
//                        .param("generationId", "13")
//                        .param("language", "Java")
//                        .param("sortType", "runtime")
//                        .param("page", "1"))
//                .andExpect(status().isOk())
//                .andDo(print());
//    }
//
//    @Test
//    @WithMockUser
//    void getAlgorithmSolution_Success() throws Exception {
//        // given
//        int solutionId = 1;
//
//        // when & then
//        mockMvc.perform(get("/api/v1/algorithm/problem/1/solution/{solutionId}", solutionId))
//                .andExpect(status().isOk())
//                .andDo(print());
//    }
//
//    @Test
//    @WithMockUser
//    void getAlgorithmSolution_Fail_NotFound() throws Exception {
//        // given
//        int solutionId = 999; // 존재하지 않는 ID
//
//        // when & then
//        mockMvc.perform(get("/api/v1/algorithm/problem/1/solution/{solutionId}", solutionId))
//                .andExpect(status().isNoContent())
//                .andDo(print());
//    }
//
//    @Test
//    @WithMockUser
//    void updateAlgorithmSolution_Success() throws Exception {
//        // given
//        int solutionId = 1;
//        AlgorithmSolution request = new AlgorithmSolution();
//        request.setTitle("수정된 제목");
//        request.setSummary("수정된 요약");
//        request.setContent("수정된 내용");
//        request.setRuntime(200);
//        request.setMemory(512);
//        request.setLanguageId(1);
//
//        // when & then
//        mockMvc.perform(put("/api/v1/algorithm/problem/1/solution/{solutionId}", solutionId)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isOk())
//                .andDo(print());
//    }
//
//    @Test
//    @WithMockUser
//    void deleteAlgorithmSolution_Success() throws Exception {
//        // given
//        int solutionId = 1;
//
//        // when & then
//        mockMvc.perform(delete("/api/v1/algorithm/problem/1/solution/{solutionId}", solutionId))
//                .andExpect(status().isOk())
//                .andDo(print());
//    }
//}
//
