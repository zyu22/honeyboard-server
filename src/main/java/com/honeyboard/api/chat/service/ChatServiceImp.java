package com.honeyboard.api.chat.service;

import com.honeyboard.api.chat.mapper.ChatMapper;
import com.honeyboard.api.chat.model.Chat;
import com.honeyboard.api.common.model.PageInfo;
import com.honeyboard.api.common.response.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatServiceImp implements ChatService {
    private final ChatMapper chatMapper;

    @Override
    public PageResponse<Chat> getChatListByGenerationId(int currentPage, int pageSize, int generationId) {
        validatePaginationParams(currentPage, pageSize);
        if (generationId <= 0) {
            throw new IllegalArgumentException("유효하지 않은 기수 ID입니다.");
        }

        try {
            int totalElement = chatMapper.countChatListByGenerationId(generationId);
            int offset = (currentPage - 1) * pageSize;
            PageInfo pageInfo = new PageInfo(currentPage, pageSize, totalElement);

            List<Chat> list = chatMapper.selectChatListByGenerationId(offset, pageSize, generationId);
            return new PageResponse<>(list, pageInfo);

        } catch (Exception e) {
            log.error("채팅 목록 조회 실패 - 기수: {}, 페이지: {}, 크기: {}, 오류: {}",
                    generationId, currentPage, pageSize, e.getMessage());
            throw new RuntimeException("채팅 목록 조회 중 오류가 발생했습니다.", e);
        }
    }

    @Override
    public boolean saveChat(Chat chat) {
        validateChat(chat);
        try {
            int result = chatMapper.insertChat(chat);
            if (result != 1) {
                throw new RuntimeException("채팅 저장에 실패했습니다.");
            }
            return true;
        } catch (Exception e) {
            log.error("채팅 저장 실패 - 사용자: {}, 내용: {}, 오류: {}",
                    chat.getUserId(), chat.getContent(), e.getMessage());
            throw new RuntimeException("채팅 저장 중 오류가 발생했습니다.", e);
        }
    }

    private void validatePaginationParams(int currentPage, int pageSize) {
        if (currentPage <= 0) {
            throw new IllegalArgumentException("페이지 번호는 1 이상이어야 합니다.");
        }
        if (pageSize <= 0) {
            throw new IllegalArgumentException("페이지 크기는 1 이상이어야 합니다.");
        }
        if (pageSize > 100) {  // 적절한 최대값 설정
            throw new IllegalArgumentException("페이지 크기가 너무 큽니다.");
        }
    }

    private void validateChat(Chat chat) {
        if (chat == null) {
            throw new IllegalArgumentException("채팅 정보가 없습니다.");
        }
        if (chat.getUserId() <= 0) {
            throw new IllegalArgumentException("유효하지 않은 사용자 ID입니다.");
        }
        if (chat.getContent() == null || chat.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("채팅 내용을 입력해주세요.");
        }
        if (chat.getGenerationId() <= 0) {
            throw new IllegalArgumentException("유효하지 않은 기수 ID입니다.");
        }
    }

}
