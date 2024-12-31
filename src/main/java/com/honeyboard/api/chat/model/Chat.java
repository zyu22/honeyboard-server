package com.honeyboard.api.chat.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Chat {
    private int id;
    private String sender;
    private int userId;
    private int generationId;
    private String content;
    private String createdAt;

    public Chat(int userId, String content) {
        this.userId = userId;
        this.content = content;
    }
}
