package com.honeyboard.api.user.mapper;

import com.honeyboard.api.user.model.Bookmark;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface BookmarkMapper {

    List<Bookmark> selectAllBookmarks(@Param("userId") int userId, @Param("contentType") String contentType);

    void insertBookmark(Bookmark bookmark);

    void updateBookmarkAsDeleted(@Param("userId") int userId, @Param("bookmarkId") int bookmarkId);
}
