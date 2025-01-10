package com.honeyboard.api.algorithm.guide.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.honeyboard.api.algorithm.guide.model.request.AlgorithmGuideRequest;
import com.honeyboard.api.algorithm.guide.model.response.AlgorithmGuideDetail;
import com.honeyboard.api.algorithm.guide.model.response.AlgorithmGuideList;
import com.honeyboard.api.algorithm.guide.service.AlgorithmGuideService;
import com.honeyboard.api.common.model.CreateResponse;
import com.honeyboard.api.common.response.PageResponse;
import com.fasterxml.jackson.core.type.TypeReference;  // 추가된 import
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)  // 시큐리티 필터 비활성화
@Import(com.honeyboard.api.config.TestConfig.class)  // 전체 패키지 경로 명시 : CurrentUser
public class AlgorithmGuideControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;  // JSON 변환을 위한 매퍼

    @Autowired
    private HandlerMethodArgumentResolver customUserArgumentResolver;

    @Mock  // @MockBean 대신 @Mock 사용
    private AlgorithmGuideService algorithmGuideService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);  // Mock 초기화
        mockMvc = MockMvcBuilders.standaloneSetup(new AlgorithmGuideController(algorithmGuideService))
                .setCustomArgumentResolvers(customUserArgumentResolver)
                .build();
    }


    @Test
    @WithMockUser  // 인증된 사용자의 테스트
    void getAllAlgorithmGuide() throws Exception {
        // given
        int currentPage = 1;
        int pageSize = 8;
        int generationId = 0; // 기본값
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
        PageResponse<AlgorithmGuideList> response = objectMapper.readValue(responseBody,
                new TypeReference<PageResponse<AlgorithmGuideList>>() {});  // 제네릭 타입을 사용하여 응답 파싱

        // 페이지 응답 검증
        assertNotNull(response);
        assertTrue(response.getContent().size() > 0, "알고리즘 가이드 목록이 비어있지 않아야 합니다.");
        System.out.println("=== 알고리즘 가이드 목록 ===");
        for (AlgorithmGuideList guide : response.getContent()) {
            System.out.println("ID: " + guide.getId());
            System.out.println("Title: " + guide.getTitle());
            System.out.println("Thumbnail: " + guide.getThumbnail());
            System.out.println("Created At: " + guide.getCreatedAt());
            System.out.println("--------------------------------");
        }
    }

    @Test
    @WithMockUser
    void getAlgorithmGuideDetail_Success() throws Exception {
        // given
        int guideId = 1;

        // when & then
        MvcResult result = mockMvc.perform(get("/api/v1/algorithm/guide/{guideId}", guideId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        AlgorithmGuideDetail response = objectMapper.readValue(responseBody, AlgorithmGuideDetail.class);

        System.out.println("\n=== 알고리즘 가이드 상세 조회 결과 ===");
        System.out.println("가이드 ID: " + response.getId());
        System.out.println("제목: " + response.getTitle());
        System.out.println("내용: " + response.getContent());
        System.out.println("작성자 ID: " + response.getAuthorId());
        System.out.println("작성자 이름: " + response.getAuthorName());
        System.out.println("북마크 여부: " + response.isBookmarked());
    }

    @Test
    @WithMockUser
    void addAlgorithmGuide_Success() throws Exception {
        // given
        AlgorithmGuideRequest request = new AlgorithmGuideRequest();
        request.setTitle("테스트 알고리즘 가이드");
        request.setContent("테스트 내용입니다.");
        request.setThumbnail("test_thumbnail.jpg");

        // when & then
        mockMvc.perform(post("/api/v1/algorithm/guide")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    @WithMockUser
    void updateAlgorithmGuide_Success() throws Exception {
        // given
        int guideId = 1;
        AlgorithmGuideRequest request = new AlgorithmGuideRequest();
        request.setTitle("수정된 알고리즘 가이드");
        request.setContent("수정된 내용입니다.");
        request.setThumbnail("updated_thumbnail.jpg");

        // when & then
        mockMvc.perform(put("/api/v1/algorithm/guide/{guideId}", guideId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockUser
    void deleteAlgorithmGuide_Success() throws Exception {
        // given
        int guideId = 1;

        // when & then
        mockMvc.perform(delete("/api/v1/algorithm/guide/{guideId}", guideId))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockUser
    void addAlgorithmGuide_Fail_InvalidRequest() throws Exception {
        // given
        AlgorithmGuideRequest request = new AlgorithmGuideRequest();
        // 필수 값을 비워둠

        // when & then
        mockMvc.perform(post("/api/v1/algorithm/guide")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
}
