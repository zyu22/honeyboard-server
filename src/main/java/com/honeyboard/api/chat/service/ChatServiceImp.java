package com.honeyboard.api.chat.service;

import com.honeyboard.api.chat.mapper.ChatMapper;
import com.honeyboard.api.chat.model.Chat;
import com.honeyboard.api.common.model.PageInfo;
import com.honeyboard.api.common.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatServiceImp implements ChatService {
    private final ChatMapper chatMapper;

    @Override
    public PageResponse<Chat> getChatListByGenerationId(int currentPage, int pageSize, int generationId) {
        int totalElement = chatMapper.countChatListByGenerationId(generationId);
        int offset = (currentPage - 1) * pageSize;
        PageInfo pageInfo = new PageInfo(currentPage, pageSize, totalElement);
        List<Chat> list = chatMapper.selectChatListByGenerationId(offset, pageSize, generationId);
        return new PageResponse<>(list, pageInfo);
    }

    @Override
    public boolean saveChat(Chat chat) {
        return chatMapper.insertChat(chat) == 1;
    }
}
