package com.honeyboard.api.bookmark.controller;

import com.amazonaws.Response;
import com.honeyboard.api.bookmark.model.BookmarkListResponse;
import com.honeyboard.api.bookmark.model.BookmarkResponse;
import com.honeyboard.api.bookmark.service.BookmarkService;
import com.honeyboard.api.user.model.CurrentUser;
import com.honeyboard.api.user.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/v1/bookmark")
@RequiredArgsConstructor
@Slf4j
public class BookmarkController {
    private final BookmarkService bookmarkService;


    // 북마크 조회
    @GetMapping("/{contentType}")
    public ResponseEntity<?> getAllBookmarks(@PathVariable String contentType,
                                             @CurrentUser User user) {

        log.info("북마크 전체 조회 요청 - 타입: {}, 유저 ID: {}", contentType, user.getUserId());

        BookmarkListResponse bookmarkListResponse = new BookmarkListResponse();
        List<?> bookmarkList = bookmarkService.getAllBookmarks(contentType, user.getUserId());
        if(bookmarkList.isEmpty() || bookmarkList == null) {
            log.info("북마크 전체 조회 완료 - 데이터 없음");
            return ResponseEntity.noContent().build();
        }

        bookmarkListResponse.setBookmarkListResponse(bookmarkList);
        log.info("북마크 전체 조회 완료 - 조회된 개수: {}", bookmarkList.size());
        return ResponseEntity.ok(bookmarkListResponse);
    }


    // 북마크 추가
    @PostMapping("/{contentType}/{contentId}")
    public ResponseEntity<?> addBookmark(@PathVariable String contentType, @PathVariable int contentId,
                                         @CurrentUser User user) {
        BookmarkResponse bookmarkResponse = new BookmarkResponse();

        log.info("북마크 추가 요청 - 타입: {}, 컨텐츠 ID: {}, 용자: {}", contentType, contentId, user.getUserId());
        bookmarkResponse.setBookmarked(bookmarkService.addBookmark(contentType, contentId, user.getUserId()));
        log.info("북마크 추가 완료 - 타입: {}, 컨텐츠 ID: {}, 용자: {}", contentType, contentId, user.getUserId());
        return ResponseEntity.ok(bookmarkResponse);
    }

    // 북마크 삭제
    @DeleteMapping("/{contentType}/{contentId}")
    public ResponseEntity<?> deleteBookmark(@PathVariable String contentType, @PathVariable int contentId,
                                            @CurrentUser User user) {
        BookmarkResponse bookmarkResponse = new BookmarkResponse();
        log.info("북마크 삭제 요청 - 타입: {}, 컨텐츠 ID: {}, 용자: {}", contentType, contentId, user.getUserId());
        bookmarkResponse.setBookmarked(bookmarkService.deleteBookmark(contentType, contentId, user.getUserId()));
        log.info("북마크 삭제 완료 - 타입: {}, 컨텐츠 ID: {}, 용자: {}", contentType, contentId, user.getUserId());

        return ResponseEntity.ok(bookmarkResponse);
    }
}
