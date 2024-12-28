package com.honeyboard.api.chat.controller;

import com.honeyboard.api.chat.service.ChatService;
import com.honeyboard.api.user.model.CurrentUser;
import com.honeyboard.api.user.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    @GetMapping
    public ResponseEntity<?> getChatListByGenerationId(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "50") int size,
            @CurrentUser User loginUser
    ) {
        int generationId = loginUser.getGenerationId();
        try {
            log.debug("채팅 목록 조회 요청 - 기수: {}, 페이지: {}, 사이즈: {}",
                    generationId, page, size);

            // 페이징 처리를 위해 offset 계산 (MyBatis용)
            int offset = (page - 1) * size;

            return ResponseEntity.ok(
                    chatService.getChatListByGenerationId(offset, size, generationId)
            );

        } catch (Exception e) {
            log.error("채팅 목록 조회 실패 - 기수: {}, 에러: {}",
                    generationId, e.getMessage(), e);

            return ResponseEntity.internalServerError()
                    .body("채팅 목록 조회 중 오류가 발생했습니다.");
        }
    }
}
