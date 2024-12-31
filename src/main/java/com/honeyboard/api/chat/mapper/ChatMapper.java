package com.honeyboard.api.chat.mapper;

import com.honeyboard.api.chat.model.Chat;

import java.util.List;

public interface ChatMapper {
    List<Chat> selectChatListByGenerationId(int offset, int pageSize, int generationId);

    int countChatListByGenerationId(int generationId);

    int insertChat(Chat chat);
}
