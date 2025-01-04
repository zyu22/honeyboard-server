package com.honeyboard.api.user.service;

import com.honeyboard.api.user.model.bookmark.Bookmark;
import java.util.List;

public interface BookmarkService {

    //타입별 북마크 조회 contentType null이면 전체 조회
    List<?> getAllBookmarks(int userId, String contentType);

    //북마크 추가
    void addBookmark(int userId, Bookmark bookmark);

    //북마크 삭제
    void deleteBookmark(int userId, String contentType ,int contentId);

}
