package com.honeyboard.api.user.controller;

import com.honeyboard.api.jwt.model.service.JwtService;
import com.honeyboard.api.user.model.bookmark.Bookmark;
import com.honeyboard.api.user.model.LogInUserResponse;
import com.honeyboard.api.user.model.User;
import com.honeyboard.api.user.model.bookmark.BookmarkedAlgorithmGuide;
import com.honeyboard.api.user.model.bookmark.BookmarkedAlgorithmSolution;
import com.honeyboard.api.user.model.mypage.MyAlgorithmSolution;
import com.honeyboard.api.user.model.mypage.MyFinaleProject;
import com.honeyboard.api.user.model.mypage.MyTrackProject;
import com.honeyboard.api.user.service.BookmarkService2;
import com.honeyboard.api.user.service.MyPageService;
import com.honeyboard.api.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerUnitTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserService userService;

    @Mock
    private BookmarkService2 bookmarkService;

    @Mock
    private MyPageService myPageService;

    @Test
    void updatePassword_Success() {
        // given
        String temporaryToken = "valid_temporary_token";
        User requestUser = new User();
        requestUser.setUserId(1);
        requestUser.setPassword("new_password");

        when(jwtService.getUserIdFromToken(temporaryToken)).thenReturn(1);

        // when
        ResponseEntity<Void> response = userController.updatePassword(temporaryToken, requestUser);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService, times(1)).updatePassword(requestUser);
    }

    @Test
    void getUserInfo_Success() {
        // given
        User user = new User();
        user.setUserId(1);
        user.setName("John");
        // ... 사용자 정보 설정

        when(userService.getUser(user.getUserId())).thenReturn(user);

        // when
        ResponseEntity<LogInUserResponse> response = userController.getUserInfo(user);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        LogInUserResponse logInUserResponse = response.getBody();
        assertEquals(user.getUserId(), logInUserResponse.getUserId());
        assertEquals(user.getName(), logInUserResponse.getName());
        // ... 필요한 검증 수행
    }

    @Test
    void getBookmarks_Success() {
        // given
        int userId = 1;
        String contentType = "project";
        List<Object> bookmarks = Arrays.asList(
                new BookmarkedAlgorithmGuide(1, "알고리즘_추천", "tempURL", "aa"),
                new BookmarkedAlgorithmSolution(2, "달이차오른다, 가자", "테스트", 150, 355, 1)
        );
//
//        when(bookmarkService.getAllBookmarks(userId, contentType)).thenReturn(bookmarks);

        // when
        ResponseEntity<?> response = userController.getBookmarks(userId, contentType);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(bookmarks, response.getBody());
    }

    @Test
    void addBookmark_Success() {
        // given
        int userId = 1;
        Bookmark bookmark = new Bookmark();
        // 북마크 정보 설정

        // when
        ResponseEntity<Void> response = userController.addBookmark(userId, bookmark);

        // then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(bookmarkService, times(1)).addBookmark(userId, bookmark);
    }

    @Test
    void deleteBookmark_Success() {
        // given
        int userId = 1;
        String contentType = "project";
        int contentId = 1;

        // when
        ResponseEntity<Void> response = userController.deleteBookmark(userId, contentType, contentId);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(bookmarkService, times(1)).deleteBookmark(userId, contentType, contentId);
    }

    @Test
    void getMyTrackProjects_Success() {
        // given
        int userId = 1;
        List<MyTrackProject> projects = new ArrayList<>();
        // 트랙 프로젝트 데이터 설정

        when(myPageService.getAllMyTrackProjects(userId)).thenReturn(projects);

        // when
        ResponseEntity<List<MyTrackProject>> response = userController.getMyTrackProjects(userId);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(projects, response.getBody());
    }

    @Test
    void getMyFinaleProjects_Success() {
        // given
        int userId = 1;
        List<MyFinaleProject> finaleProjects = new ArrayList<>();
        // 파이널 프로젝트 데이터 설정

        when(myPageService.getAllMyFinaleProjects(userId)).thenReturn(finaleProjects);

        // when
        ResponseEntity<List<MyFinaleProject>> response = userController.getMyFinaleProjects(userId);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(finaleProjects, response.getBody());
    }

    @Test
    void getMyAlgorithms_Success() {
        // given
        int userId = 1;
        List<MyAlgorithmSolution> solutions = new ArrayList<>();
        // 알고리즘 풀이 데이터 설정

        when(myPageService.getAllMyAlgorithms(userId)).thenReturn(solutions);

        // when
        ResponseEntity<List<MyAlgorithmSolution>> response = userController.getMyAlgorithms(userId);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(solutions, response.getBody());
    }
}