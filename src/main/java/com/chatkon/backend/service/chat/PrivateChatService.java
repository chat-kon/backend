package com.chatkon.backend.service.chat;

import com.chatkon.backend.model.entity.chat.PrivateChat;

import java.util.Optional;

public interface PrivateChatService {

    PrivateChat findPrivateChat(Long chatId);

    PrivateChat createPrivateChat(Long user1Id, Long user2Id);

    Optional<PrivateChat> findPrivateChat(Long user1Id, Long user2Id);
}
