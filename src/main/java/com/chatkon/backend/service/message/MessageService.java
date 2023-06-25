package com.chatkon.backend.service.message;

import com.chatkon.backend.model.entity.message.BinaryMessage;
import com.chatkon.backend.model.entity.message.Message;
import com.chatkon.backend.model.entity.message.TextMessage;

public interface MessageService {
    TextMessage createText(Long chatId, Long senderId, TextMessage textMessage);

    BinaryMessage saveFile(BinaryMessage binaryMessage);

    Message getLastMessage(Long chatId);

    Long deleteMessage(Long deleterId, Long messageId);

    Message findMessage(Long messageId);

    Double rateMessage(Long userId, Long messageId, Double rate);
}
