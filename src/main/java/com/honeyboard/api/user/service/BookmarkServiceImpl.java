package com.honeyboard.api.user.service;

import com.honeyboard.api.user.model.Bookmark;
import com.honeyboard.api.user.mapper.BookmarkMapper;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BookmarkServiceImpl implements BookmarkService {

    private final BookmarkMapper bookmarkMapper;

    public BookmarkServiceImpl(BookmarkMapper bookmarkMapper) {
        this.bookmarkMapper = bookmarkMapper;
    }

    @Override
    public List<Bookmark> getAllBookmarks(int userId, String contentType) {
        return bookmarkMapper.selectAllBookmarks(userId, contentType);
    }

    @Override
    public void addBookmark(int userId, Bookmark bookmark) {
        bookmark.setUserId(userId);
        bookmarkMapper.insertBookmark(bookmark);
    }

    @Override
    public void softDeleteBookmark(int userId, int bookmarkId) {
        bookmarkMapper.updateBookmarkAsDeleted(userId, bookmarkId);
    }


}
