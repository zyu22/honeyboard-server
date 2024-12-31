package com.honeyboard.api.chat.controller;

import com.honeyboard.api.chat.model.Chat;
import com.honeyboard.api.chat.service.ChatService;
import com.honeyboard.api.common.response.PageResponse;
import com.honeyboard.api.user.model.CurrentUser;
import com.honeyboard.api.user.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
        log.debug("채팅 목록 조회 요청 - 기수: {}, 페이지: {}, 사이즈: {}",
                loginUser.getGenerationId(), page, size);

        PageResponse<Chat> response = chatService.getChatListByGenerationId(
                page,
                size,
                loginUser.getGenerationId()
        );

        return response.getContent().isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(response);
    }

}
