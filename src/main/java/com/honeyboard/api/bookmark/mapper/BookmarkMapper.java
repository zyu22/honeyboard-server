package com.honeyboard.api.bookmark.mapper;

import com.honeyboard.api.algorithm.guide.model.response.AlgorithmGuideList;
import com.honeyboard.api.user.model.bookmark.BookmarkedAlgorithmGuide;
import com.honeyboard.api.user.model.bookmark.BookmarkedAlgorithmSolution;
import com.honeyboard.api.user.model.bookmark.BookmarkedWebGuide;
import com.honeyboard.api.user.model.bookmark.BookmarkedWebRecommendation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BookmarkMapper {

    // 알고리즘 가이드 북마크 조회
    List<AlgorithmGuideList> selectAllAlgorithmGuideBookmarks(@Param("userId") int userId);

    // 알고리즘 문제풀이 북마크 조회
    List<BookmarkedAlgorithmSolution> selectAllAlgorithmSolutionBookmarks(@Param("userId") int userId);

    // 웹 가이드 북마크 조회
    List<BookmarkedWebGuide> selectAllWebGuideBookmarks(@Param("userId") int userId);

    // 웹 추천 북마크 조회
    List<BookmarkedWebRecommendation> selectAllWebRecommendBookmarks(@Param("userId") int userId);

    // 북마크 추가
    int insertBookmark(@Param("contentType") String contentType, @Param("contentId") int contentId, @Param("userId") int userId);

    // 북마크 삭제
    int deleteBookmark(@Param("contentType") String contentType, @Param("contentId") int contentId, @Param("userId") int userId);
}
