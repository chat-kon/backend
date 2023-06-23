package com.chatkon.backend.service.chat;

import com.chatkon.backend.model.entity.chat.Chat;
import com.chatkon.backend.model.entity.message.Message;

import java.util.Set;

public interface ChatService {
    Chat findChat(Long chatId);

    Set<Message> getChatMessages(Long userId, Long chatId);

    void deleteChat(Long userId, Long chatId);
}
