package com.honeyboard.api.bookmark.service;

import com.honeyboard.api.bookmark.mapper.BookmarkMapper;
import com.honeyboard.api.bookmark.model.BookmarkResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookmarkServiceImpl implements BookmarkService {
    private final BookmarkMapper bookmarkMapper;

    // 북마크 조회
    @Override
    public List<?> getAllBookmarks(String contentType, int userId) {
        // 유효성 검증
        validateInput(contentType, userId);

        log.info("북마크 전체 조회 시작 - 유저ID: {}, 컨텐츠 타입: {}", userId, contentType);

        contentType = contentType.trim().toUpperCase();
        switch (contentType) {
            case "ALGO_GUIDE":
                return bookmarkMapper.selectAllAlgorithmGuideBookmarks(userId);
            case "ALGO_SOLUTION":
                return bookmarkMapper.selectAllAlgorithmSolutionBookmarks(userId);
            case "WEB_GUIDE":
                return bookmarkMapper.selectAllWebGuideBookmarks(userId);
            case "WEB_RECOMMEND":
                return bookmarkMapper.selectAllWebRecommendBookmarks(userId);
            default:
                log.warn("지원하지 않는 contentType입니다. contentType: {}", contentType);
                return Collections.emptyList();
        }
    }

    @Override
    public boolean addBookmark(String contentType, int contentId, int userId) {
        // 유효성 검증
        validateInput(contentType, userId);

        log.info("북마크 추가 시작 - 타입: {}, 컨텐츠 ID: {}, 유저ID: {}", contentType, contentId, userId);
        int result = bookmarkMapper.insertBookmark(contentType, contentId, userId);

        if(result <= 0) {
            log.error("북마크 추가 실패 - 유저ID: {}", userId);
            throw new RuntimeException("이미 추가된 북마크입니다.");
        }

        log.info("북마크 추가 성공 - 타입: {}, 컨텐츠 ID: {}, 유저ID: {}", contentType, contentId, userId);

        return result == 1;
    }

    @Override
    public boolean deleteBookmark(String contentType, int contentId, int userId) {
        // 유효성 검증
        validateInput(contentType, userId);
        log.info("북마크 삭제 시작 - 타입: {}, 컨텐츠 ID: {}, 유저ID: {}", contentType, contentId, userId);
        int result = bookmarkMapper.deleteBookmark(contentType, contentId, userId);

        if(result <= 0) {
            log.error("북마크 삭제 실패 - 유저ID: {}", userId);
            throw new RuntimeException("이미 삭제된 북마크입니다.");
        }

        log.info("북마크 삭제 성공 - 타입: {}, 컨텐츠 ID: {}, 유저ID: {}", contentType, contentId, userId);

        return result == 1;
    }

    private void validateInput(String contentType, int userId) {
        // 파라미터 검증
        if (userId <= 0) {
            log.warn("유효하지 않은 유저 ID입니다. userId={}", userId);
            throw new IllegalArgumentException("유효하지 않은 사용자 ID입니다.");
        }

        if (contentType == null || contentType.trim().isEmpty()) {
            log.warn("contentType이 비어있습니다. userId: {}", userId);
            throw new IllegalArgumentException("유효하지 않은 contentType입니다.");
        }
    }
}
