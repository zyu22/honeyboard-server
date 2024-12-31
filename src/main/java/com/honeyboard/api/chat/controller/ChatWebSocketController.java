package com.honeyboard.api.chat.controller;


import com.honeyboard.api.chat.model.Chat;
import com.honeyboard.api.chat.service.ChatService;
import com.honeyboard.api.user.model.User;
import com.honeyboard.api.user.model.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j  // lombok 로깅 어노테이션 추가
@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {
    private static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final String TOPIC_PREFIX = "/topic/generation/";
    private static final String ERROR_QUEUE = "/queue/errors";
    private final UserService userService;
    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.send")
    public void sendMessage(Chat chat) {
        try {
            // 입력값 검증
            if (chat.getContent() == null || chat.getContent().trim().isEmpty()) {
                throw new IllegalArgumentException("메시지 내용이 비어있습니다.");
            }

            User user = userService.getUser(chat.getUserId());

            chat.setSender(user.getName());
            chat.setGenerationId(user.getGenerationId());
            chat.setCreatedAt(LocalDateTime.now().format(
                    DateTimeFormatter.ofPattern(DATE_FORMAT_PATTERN))
            );

            boolean res = chatService.saveChat(chat);
            if (!res) {
                throw new RuntimeException("메시지 저장에 실패했습니다.");
            }

            // 로깅 추가
            log.debug("채팅 메시지 전송 완료 - 메시지 ID: {}, 발신자: {}, 기수: {}",
                    chat.getId(),
                    chat.getSender(),
                    chat.getGenerationId()
            );

            messagingTemplate.convertAndSend(
                    TOPIC_PREFIX + user.getGenerationId(),
                    chat
            );

        } catch (Exception e) {
            log.error("채팅 메시지 전송 실패 - 사용자 ID: {}, 에러 메시지: {}",
                    chat.getUserId(),
                    e.getMessage(),
                    e
            );

            messagingTemplate.convertAndSendToUser(
                    String.valueOf(chat.getUserId()),
                    ERROR_QUEUE,
                    "메시지 전송 실패: " + e.getMessage()
            );
        }
    }
}