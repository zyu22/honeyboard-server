package com.honeyboard.api.user.mapper;


import com.honeyboard.api.user.model.bookmark.*;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BookmarkMapper {


    // 1) 알고리즘 가이드 북마크 조회
    List<BookmarkedAlgorithmGuide> selectAllAlgorithmGuideBookmarks(@Param("userId") int userId);

    // 2) 알고리즘 문제풀이 북마크 조회
    List<BookmarkedAlgorithmSolution> selectAllAlgorithmSolutionBookmarks(@Param("userId") int userId);

    // 3) 웹 가이드 북마크 조회
    List<BookmarkedWebGuide> selectAllWebGuideBookmarks(@Param("userId") int userId);

    // 4) 웹 추천 북마크 조회
    List<BookmarkedWebRecommendation> selectAllWebRecommendBookmarks(@Param("userId") int userId);


    // bookmark 테이블에 북마크 추가
    int insertBookmark(Bookmark bookmark);

    // bookmark 테이블에서 북마크 삭제
    int deleteBookmark(
            @Param("userId") int userId,
            @Param("contentType") String contentType,
            @Param("contentId") int contentId
    );}
