package com.honeyboard.api.web.guide.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.honeyboard.api.common.model.CreateResponse;
import com.honeyboard.api.common.response.PageResponse;
import com.honeyboard.api.config.WithMockCustomUser;
import com.honeyboard.api.jwt.model.service.JwtService;
import com.honeyboard.api.user.model.User;
import com.honeyboard.api.user.service.UserService;
import com.honeyboard.api.web.guide.model.request.WebGuideRequest;
import com.honeyboard.api.web.guide.model.response.WebGuideDetail;
import com.honeyboard.api.web.guide.model.response.WebGuideList;

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
    
    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;
    
//    @Test
//    @WithMockUser
//    void getAllWebGuide_Success() throws Exception {
//        // given
//        int generationId = 13;
//        int currentPage = 1;
//        int pageSize = 8;
//
//        // when & then
//        MvcResult result = mockMvc.perform(get("/api/v1/web/guide")
//                        .param("generationId", String.valueOf(generationId))
//                        .param("currentPage", String.valueOf(currentPage))
//                        .param("pageSize", String.valueOf(pageSize))
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andDo(print())
//                .andReturn();
//
//        PageResponse<WebGuideList> response = objectMapper.readValue(
//                result.getResponse().getContentAsString(),
//                objectMapper.getTypeFactory().constructParametricType(PageResponse.class, WebGuideList.class)
//        );
//
//        System.out.println("\n=== 웹 개념 전체 조회 결과 ===");
//        for (WebGuideList guide : response.getContent()) {
//            System.out.println("개념 ID: " + guide.getId());
//            System.out.println("제목: " + guide.getTitle());
//            System.out.println("-------------------------");
//        }
//    }

//    @Test
//    @WithMockUser
//    void searchWebGuide_Success() throws Exception {
//        // given
//        int generationId = 13;
//        String title = "프론트엔드";
//        int currentPage = 1;
//        int pageSize = 8;
//
//        // when & then
//        MvcResult result = mockMvc.perform(get("/api/v1/web/guide")
//                        .param("generationId", String.valueOf(generationId))
//                        .param("currentPage", String.valueOf(currentPage))
//                        .param("pageSize", String.valueOf(pageSize))
//                        .param("title", title)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andDo(print())
//                .andReturn();
//        
//        PageResponse<WebGuideList> response = objectMapper.readValue(
//              result.getResponse().getContentAsString(),
//              objectMapper.getTypeFactory().constructParametricType(PageResponse.class, WebGuideList.class)
//      );
//
//      System.out.println("\n=== 웹 개념 전체 조회 결과 ===");
//      for (WebGuideList guide : response.getContent()) {
//          System.out.println("개념 ID: " + guide.getId());
//          System.out.println("제목: " + guide.getTitle());
//          System.out.println("-------------------------");
//      }
//    }

    // 문제-----------------
//    @Test
//    @WithMockUser
//    void getWebGuide_Success() throws Exception {
//        // given
//        int guideId = 1;
//
//        // when & then
//        MvcResult result = mockMvc.perform(get("/api/v1/web/guide/{guideId}", guideId)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andDo(print())
//                .andReturn();
//        
//        WebGuideDetail response = objectMapper.readValue(
//              result.getResponse().getContentAsString(), WebGuideDetail.class
//        );
//        
//        System.out.println("\n=== 웹 개념 상세 조회 결과 ===");
//        System.out.println("개념 ID: " + response.getId());
//        System.out.println("제목: " + response.getTitle());
//        System.out.println("-------------------------");
//    }

    @Test
    @WithMockCustomUser(id = 5, email = "sktndid1203@gmail.com", name = "박수양", role = "ADMIN", generationId = 13)
    void getWebGuide_Success() throws Exception {
        // given
        int guideId = 41;
        
        // when & then
        MvcResult result = mockMvc.perform(get("/api/v1/web/guide/{guideId}", guideId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        WebGuideDetail response = objectMapper.readValue(
            result.getResponse().getContentAsString(), WebGuideDetail.class
        );

        System.out.println("\n=== 웹 개념 상세 조회 결과 ===");
        System.out.println("개념 ID: " + response.getId());
        System.out.println("제목: " + response.getTitle());
        System.out.println("-------------------------");
    }
    
    // 웹 개념 작성 테스트
    @Test
    @WithMockUser
    void createWebGuide_Success() throws Exception {
        // given
        WebGuideRequest request = new WebGuideRequest();
        request.setTitle("CSS 기초");
        request.setContent("CSS인데요.");
        request.setThumbnail("www.naver.com");

        // when & then
        MvcResult result = mockMvc.perform(post("/api/v1/web/guide")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andDo(print())
            .andReturn();
        
        CreateResponse createResponse = objectMapper.readValue(
                result.getResponse().getContentAsString(), CreateResponse.class
        );

        System.out.println("\n=== 웹 개념 작성 결과 ===");
        System.out.println("개념 ID: " + createResponse.getId());
        System.out.println("-------------------------");
    }

    // 웹 개념 수정 테스트
    @Test
    @WithMockUser
    void updateWebGuide_Success() throws Exception {
        // given
        int guideId = 4;
        WebGuideRequest request = new WebGuideRequest();
        request.setTitle("통합테스트용 가이드");
        request.setContent("통합테스트 내용입니다.");
        request.setThumbnail("test.jpg");
        
        // when & then
        mockMvc.perform(put("/api/v1/web/guide/{guideId}", guideId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    // 웹 개념 삭제 테스트
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

    // 잘못된 요청에 대한 테스트 (예: 빈 필드로 요청)
    @Test
    @WithMockUser
    void addWebGuide_Fail_InvalidRequest() throws Exception {
        // given
        WebGuideRequest request = new WebGuideRequest();
        // 필수 값을 비워둠

        // when & then
        mockMvc.perform(post("/api/v1/web/guide")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
}