package com.honeyboard.api.user.controller;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
////    @Test
////    @WithMockUser
////    void updatePassword_Success() throws Exception {
////        // given
////        String temporaryToken = "valid_temporary_token";
////        User requestUser = new User();
////        requestUser.setPassword("new_password");
////
////        // when & then
////        mockMvc.perform(post("/api/v1/user/reset-password")
////                        .cookie(new Cookie("temporary_token", temporaryToken))
////                        .contentType(MediaType.APPLICATION_JSON)
////                        .content(objectMapper.writeValueAsString(requestUser)))
////                .andExpect(status().isOk())
////                .andDo(print());
////    }
//
//    @Test
//    @WithMockUser
//    void getUserInfo_Success() throws Exception {
//        // given
//        int userId = 1;
//
//        // when
//        MvcResult result = mockMvc.perform(get("/api/v1/user/info")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andDo(print())
//                .andReturn();
//
//        // then
//        String responseBody = result.getResponse().getContentAsString();
//        LogInUserResponse response = objectMapper.readValue(responseBody, LogInUserResponse.class);
//
//        System.out.println("사용자 ID: " + response.getUserId());
//        System.out.println("사용자 이름: " + response.getName());
//        // ... 필요한 정보 출력
//    }
//
//    @Test
//    @WithMockUser
//    void getBookmarks_Success() throws Exception {
//        // given
//        int userId = 8;
//        String contentType = "web_guide";
//        // web_guide
//        // web_recommend
//        // algo_guide
//        // algo_solution
//
//
//        // when
//        MvcResult result = mockMvc.perform(get("/api/v1/user/{userId}/bookmark", userId)
//                        .param("contentType", contentType)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andDo(print())
//                .andReturn();
//
//        // then
//        String responseBody = result.getResponse().getContentAsString();
//        List<?> bookmarks = objectMapper.readValue(responseBody, List.class);
//
//        System.out.println("\n=== 북마크 조회 결과 ===");
//        System.out.println("사용자 ID: " + userId);
//        System.out.println("컨텐츠 타입: " + contentType);
//        System.out.println("북마크 개수: " + bookmarks.size());
//        for (Object bookmark : bookmarks) {
//            System.out.println("------------------------");
//            System.out.println("북마크 정보: " + bookmark);
//        }
//    }
//
//    @Test
//    @WithMockUser
//    void addBookmark_Success() throws Exception {
//        // given
//        int userId = 8;
//        String contentType = "web_guide";
//        int contentId = 1;
//
//        Bookmark bookmark = new Bookmark();
//        bookmark.setContentId(contentId);
//        bookmark.setContentType(contentType);
//        // when & then
//        mockMvc.perform(post("/api/v1/user/{userId}/bookmark", userId)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(bookmark)))
//                .andExpect(status().isCreated())
//                .andDo(print());
//    }
//
//    @Test
//    @WithMockUser
//    void deleteBookmark_Success() throws Exception {
//        // given
//        int userId = 8;
//        String contentType = "web_guide";
//        int contentId = 1;
//        // web_guide
//        // web_recommend
//        // algo_guide
//        // algo_solution
//
//        // when & then
//        mockMvc.perform(delete("/api/v1/user/{userId}/bookmark/{contentType}/{contentId}", userId, contentType, contentId))
//                .andExpect(status().isOk())
//                .andDo(print());
//    }
//
//    @Test
//    @WithMockUser
//    void getMyTrackProjects_Success() throws Exception {
//        // given
//        int userId = 1;
//
//        // when
//        MvcResult result = mockMvc.perform(get("/api/v1/user/{userId}/trackproject", userId)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andDo(print())
//                .andReturn();
//
//        // then
//        String responseBody = result.getResponse().getContentAsString();
//        List<MyTrackProject> projects = objectMapper.readValue(responseBody,
//                objectMapper.getTypeFactory().constructCollectionType(List.class, MyTrackProject.class));
//
//        System.out.println("\n=== 나의 트랙 프로젝트 조회 결과 ===");
//        System.out.println("사용자 ID: " + userId);
//        System.out.println("트랙 프로젝트 개수: " + projects.size());
//        for (MyTrackProject project : projects) {
//            System.out.println("------------------------");
//            System.out.println("프로젝트 게시글 ID: " + project.getTrackProjectBoardId());
//            System.out.println("프로젝트 제목: " + project.getTrackProjectTitle());
//            // ... 필요한 정보 출력
//        }
//    }
//
//    @Test
//    @WithMockUser
//    void getMyFinaleProjects_Success() throws Exception {
//        // given
//        int userId = 1;
//
//        // when
//        MvcResult result = mockMvc.perform(get("/api/v1/user/{userId}/finaleproject", userId)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andDo(print())
//                .andReturn();
//
//        // then
//        String responseBody = result.getResponse().getContentAsString();
//        List<MyFinaleProject> finaleProjects = objectMapper.readValue(responseBody,
//                objectMapper.getTypeFactory().constructCollectionType(List.class, MyFinaleProject.class));
//
//        System.out.println("\n=== 나의 파이널 프로젝트 조회 결과 ===");
//        System.out.println("사용자 ID: " + userId);
//        System.out.println("파이널 프로젝트 개수: " + finaleProjects.size());
//        for (MyFinaleProject project : finaleProjects) {
//            System.out.println("------------------------");
//            System.out.println("프로젝트 ID: " + project.getFinaleProjectBoardId());
//            System.out.println("프로젝트 제목: " + project.getTitle());
//            // ... 필요한 정보 출력
//        }
//    }
//
//    @Test
//    @WithMockUser
//    void getMyAlgorithms_Success() throws Exception {
//        // given
//        int userId = 1;
//
//        // when
//        MvcResult result = mockMvc.perform(get("/api/v1/user/{userId}/algorithm", userId)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andDo(print())
//                .andReturn();
//
//        // then
//        String responseBody = result.getResponse().getContentAsString();
//        List<MyAlgorithmSolution> solutions = objectMapper.readValue(responseBody,
//                objectMapper.getTypeFactory().constructCollectionType(List.class, MyAlgorithmSolution.class));
//
//        System.out.println("\n=== 나의 알고리즘 조회 결과 ===");
//        System.out.println("사용자 ID: " + userId);
//        System.out.println("알고리즘 풀이 개수: " + solutions.size());
//        for (MyAlgorithmSolution solution : solutions) {
//            System.out.println("------------------------");
//            System.out.println("알고리즘 ID: " + solution.getSolutionId());
//            System.out.println("알고리즘 제목: " + solution.getSolutionTitle());
//            // ... 필요한 정보 출력
//        }
//    }
}
