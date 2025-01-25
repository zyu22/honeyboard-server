package com.honeyboard.api.bookmark.service;

import com.honeyboard.api.bookmark.mapper.BookmarkMapper;
import com.honeyboard.api.bookmark.model.BookmarkListResponse;
import com.honeyboard.api.bookmark.model.ContentType;
import com.honeyboard.api.exception.BusinessException;
import com.honeyboard.api.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookmarkServiceImpl implements BookmarkService {
    private final BookmarkMapper bookmarkMapper;

    // 북마크 조회
    @Override
    public BookmarkListResponse getAllBookmarks(String contentType, int userId) {
        validateUserId(userId);
        ContentType type = ContentType.from(contentType);
        log.info("북마크 전체 조회 시작 - 유저ID: {}, 컨텐츠 타입: {}", userId, contentType);

        List<?> bookmarks = switch (type) {
            case ALGO_GUIDE -> bookmarkMapper.selectAllAlgorithmGuideBookmarks(userId);
            case ALGO_SOLUTION -> bookmarkMapper.selectAllAlgorithmSolutionBookmarks(userId);
            case WEB_GUIDE -> bookmarkMapper.selectAllWebGuideBookmarks(userId);
            case WEB_RECOMMEND -> bookmarkMapper.selectAllWebRecommendBookmarks(userId);
            default -> throw new BusinessException(ErrorCode.INVALID_CONTENT_TYPE);
        };

        BookmarkListResponse response = new BookmarkListResponse();
        response.setContent(bookmarks);
        return response;
    }

    @Override
    public boolean addBookmark(String contentType, int contentId, int userId) {
        // 유효성 검증
        validateUserId(userId);
        validateContentId(contentId);

        ContentType type = ContentType.from(contentType);

        log.info("북마크 추가 시작 - 타입: {}, 컨텐츠 ID: {}, 유저ID: {}", contentType, contentId, userId);
        int result = bookmarkMapper.insertBookmark(contentType, contentId, userId);

        if (result <= 0) {
            log.error("북마크 추가 실패 - 유저ID: {}", userId);
            throw new RuntimeException("이미 추가된 북마크입니다.");
        }

        log.info("북마크 추가 성공 - 타입: {}, 컨텐츠 ID: {}, 유저ID: {}", contentType, contentId, userId);

        return result == 1;
    }

    @Override
    public boolean deleteBookmark(String contentType, int contentId, int userId) {
        // 유효성 검증
        validateUserId(userId);
        validateContentId(contentId);

        ContentType type = ContentType.from(contentType);
        log.info("북마크 삭제 시작 - 타입: {}, 컨텐츠 ID: {}, 유저ID: {}", contentType, contentId, userId);
        int result = bookmarkMapper.deleteBookmark(contentType, contentId, userId);

        if (result <= 0) {
            log.error("북마크 삭제 실패 - 유저ID: {}", userId);
            throw new RuntimeException("이미 삭제된 북마크입니다.");
        }

        log.info("북마크 삭제 성공 - 타입: {}, 컨텐츠 ID: {}, 유저ID: {}", contentType, contentId, userId);

        return result == 1;
    }

    private void validateUserId(int userId) {
        // 파라미터 검증
        if (userId <= 0) {
            log.warn("유효하지 않은 유저 ID입니다. userId={}", userId);
            throw new IllegalArgumentException("유효하지 않은 사용자 ID입니다.");
        }
    }

    private void validateContentId(int contentId) {
        if (contentId <= 0) {
            throw new BusinessException(ErrorCode.INVALID_CONTENT_ID, "유효하지 않은 컨텐츠 ID입니다.");
        }
    }
}
