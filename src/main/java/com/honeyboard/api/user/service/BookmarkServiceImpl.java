package com.honeyboard.api.user.service;

import com.honeyboard.api.user.mapper.BookmarkMapper;
import com.honeyboard.api.user.model.Bookmark;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class BookmarkServiceImpl implements BookmarkService {

    private final BookmarkMapper bookmarkMapper;

    public BookmarkServiceImpl(BookmarkMapper bookmarkMapper) {
        this.bookmarkMapper = bookmarkMapper;
    }

    @Override
    public List<?> getAllBookmarks(int userId, String contentType) {
        // 파라미터 검증
        if (userId <= 0) {
            log.warn("유효하지 않은 유저 ID입니다. userId={}", userId);
            throw new IllegalArgumentException("유효하지 않은 사용자 ID입니다.");
        }
        if (contentType == null || contentType.trim().isEmpty()) {
            log.warn("contentType이 비어있습니다. userId={}", userId);
            throw new IllegalArgumentException("유효하지 않은 contentType입니다.");
        }

        // 조회
        try {
            log.info("[Bookmark] 조회 시작 - userId={}, contentType={}", userId, contentType);

            switch (contentType) {
                case "algo_guide":
                    return bookmarkMapper.selectAllAlgorithmGuideBookmarks(userId);

                case "algo_solution":
                    return bookmarkMapper.selectAllAlgorithmSolutionBookmarks(userId);

                case "web_guide":
                    return bookmarkMapper.selectAllWebGuideBookmarks(userId);

                case "web_recommend":
                    return bookmarkMapper.selectAllWebRecommendBookmarks(userId);

                default:

                    log.warn("지원하지 않는 contentType입니다. contentType={}", contentType);
                    return Collections.emptyList();
            }

        } catch (Exception e) {
            log.error("[Bookmark] 조회 실패 - userId={}, contentType={}, 오류: {}",
                    userId, contentType, e.getMessage(), e);
            throw new RuntimeException("북마크 조회 중 오류가 발생했습니다.", e);
        }
    }

    @Override
    public void addBookmark(int userId, Bookmark bookmark) {
        // 파라미터 검증
        if (userId <= 0) {
            log.warn("유효하지 않은 유저 ID입니다. userId={}", userId);
            throw new IllegalArgumentException("유효하지 않은 사용자 ID입니다.");
        }
        if (bookmark == null) {
            log.warn("Bookmark 객체가 null입니다.");
            throw new IllegalArgumentException("Bookmark 정보가 필요합니다.");
        }
        if (bookmark.getContentType() == null || bookmark.getContentType().trim().isEmpty()) {
            throw new IllegalArgumentException("contentType은 필수입니다.");
        }
        if (!bookmark.getContentType().equals("web_recommend")&&!bookmark.getContentType().equals("web_guide")&&
            !bookmark.getContentType().equals("algo_solution")&&!bookmark.getContentType().equals("algo_guide")){
            throw new IllegalArgumentException("지정되지 않은 contentType입니다.");
        }


        // 등록
        try {
            bookmark.setUserId(userId);
            log.info("[Bookmark] 추가 시작 - userId={}, contentType={}, contentId={}",
                    userId, bookmark.getContentType(), bookmark.getContentId());

            int rowCount = bookmarkMapper.insertBookmark(bookmark);

            if (rowCount == 0) {
                log.error("[Bookmark] 추가 실패 - userId={}, rowCount={}", userId, rowCount);
                throw new RuntimeException("이미 추가된 북마크입니다.  (rowCount=" + rowCount + ")");
            }

            log.info("[Bookmark] 추가 성공 - userId={}, contentType={}, contentId={}",
                    userId, bookmark.getContentType(), bookmark.getContentId());

        } catch (Exception e) {
            log.error("[Bookmark] 추가 실패 - userId={}, contentType={}, contentId={}, 오류: {}",
                    userId, bookmark.getContentType(), bookmark.getContentId(), e.getMessage(), e);
            throw new RuntimeException("북마크 추가 중 오류가 발생했습니다.", e);
        }
    }

    @Override
    public void deleteBookmark(int userId, int bookmarkId) {

        //파라미터 검증
        if (userId <= 0) {
            log.warn("유효하지 않은 유저 ID입니다. userId={}", userId);
            throw new IllegalArgumentException("유효하지 않은 사용자 ID입니다.");
        }
        if (bookmarkId <= 0) {
            log.warn("유효하지 않은 bookmark ID입니다. bookmarkId={}", bookmarkId);
            throw new IllegalArgumentException("유효하지 않은 bookmark ID입니다.");
        }

        // 삭제
        try {
            log.info("[Bookmark] 삭제 시작 - userId={}, bookmarkId={}", userId, bookmarkId);

            int rowCount = bookmarkMapper.deleteBookmark(userId, bookmarkId);

            if (rowCount == 0) {

                log.warn("[Bookmark] 삭제 대상이 없음 - userId={}, bookmarkId={}", userId, bookmarkId);

                throw new RuntimeException("삭제할 북마크가 존재하지 않습니다. (bookmarkId=" + bookmarkId + ")");

            }

            log.info("[Bookmark] 삭제 성공 - userId={}, bookmarkId={}", userId, bookmarkId);

        } catch (Exception e) {
            log.error("[Bookmark] 삭제 실패 - userId={}, bookmarkId={}, 오류: {}",
                    userId, bookmarkId, e.getMessage(), e);
            throw new RuntimeException("북마크 삭제 중 오류가 발생했습니다.", e);
        }
    }
}
