package com.chatkon.backend.service.message;

import com.chatkon.backend.model.entity.message.BinaryMessage;
import com.chatkon.backend.model.entity.message.Message;
import com.chatkon.backend.model.entity.message.MessageRate;
import com.chatkon.backend.model.entity.message.TextMessage;

public interface MessageService {
    TextMessage createText(Long chatId, Long senderId, TextMessage textMessage);

    BinaryMessage saveFile(Long chatId, Long senderId, BinaryMessage binaryMessage);

    Message getLastMessage(Long chatId);

    Long deleteMessage(Long deleterId, Long messageId);

    Message findMessage(Long messageId);

    MessageRate rateMessage(Long userId, Long messageId, Double rate);

    Double getAverageRate(Long messageId);

    Double getUserRateOnMessage(Long userId, Long messageId);

    Long getMessageViewCount(Long messageId);

    TextMessage forwardMessage(Long userId, Long messageId, Long chatId);
}
