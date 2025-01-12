package com.honeyboard.api.bookmark.service;

import com.honeyboard.api.bookmark.model.BookmarkResponse;
import com.honeyboard.api.user.model.bookmark.Bookmark;

import java.awt.print.Book;
import java.util.List;

public interface BookmarkService {

    // 북마크 전체 조회
    List<?> getAllBookmarks(String contentType, int userId);

    // 북마크 추가
    boolean addBookmark(String contentType, int contentId, int userId);

    // 북마크 삭제
    boolean deleteBookmark(String contentType, int contentId, int userId);
}
