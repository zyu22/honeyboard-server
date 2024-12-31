package com.honeyboard.api.chat.service;

import com.honeyboard.api.chat.model.Chat;
import com.honeyboard.api.common.response.PageResponse;

public interface ChatService {
    PageResponse<Chat> getChatListByGenerationId(int currentPage, int pageSize, int generationId);

    boolean saveChat(Chat chat);
}
