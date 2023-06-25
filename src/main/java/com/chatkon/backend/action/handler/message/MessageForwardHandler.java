package com.chatkon.backend.action.handler.message;


import com.chatkon.backend.action.Action;
import com.chatkon.backend.action.ActionResult;
import com.chatkon.backend.action.ActionType;
import com.chatkon.backend.action.handler.ActionHandler;
import com.chatkon.backend.exception.NotImplementedException;
import com.chatkon.backend.model.dto.message.MessageDto;
import com.chatkon.backend.model.dto.message.MessageForwardRequestDto;
import com.chatkon.backend.model.dto.message.MessageForwardResponseDto;
import com.chatkon.backend.model.dto.message.MessageReplayResponseDto;
import com.chatkon.backend.model.dto.user.UserForwardDto;
import com.chatkon.backend.model.entity.chat.Chat;
import com.chatkon.backend.model.entity.chat.PrivateChat;
import com.chatkon.backend.model.entity.chat.PublicChat;
import com.chatkon.backend.model.entity.message.Message;
import com.chatkon.backend.model.entity.message.TextMessage;
import com.chatkon.backend.model.entity.user.User;
import com.chatkon.backend.service.chat.ChatService;
import com.chatkon.backend.service.chat.PublicChatService;
import com.chatkon.backend.service.message.MessageService;
import com.chatkon.backend.util.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MessageForwardHandler implements ActionHandler<MessageForwardRequestDto> {
    private final MessageService messageService;
    private final ChatService chatService;
    private final PublicChatService publicChatService;

    @Override
    public ActionType type() {
        return ActionType.FORWARD_MESSAGE;
    }

    @Override
    public ActionResult handle(Long userId, MessageForwardRequestDto dto) {
        Chat chat = chatService.findChat(dto.getChatId());
        Set<Long> receivers = getReceivers(chat);

        var message = messageService.forwardMessage(userId, dto.getMessageId(), dto.getChatId());
        MessageDto messageDto = getMessageDto(userId, message);

        MessageForwardResponseDto responseDto = MessageForwardResponseDto.builder()
                .message(messageDto)
                .build();

        Action action = Action.builder()
                .dto(responseDto)
                .type(ActionType.FORWARD_MESSAGE)
                .build();

        return ActionResult.builder()
                .action(action)
                .receivers(receivers)
                .build();
    }

    private MessageDto getMessageDto(Long userId, TextMessage message) {
        MessageDto messageDto = Mapper.map(message, MessageDto.class);

        User messageSender = message.getForwardMessageRef().getSender();
        var userForwardFrom = UserForwardDto.builder()
                .id(messageSender.getId())
                .name(messageSender.getName())
                .build();

        messageDto.setUserForwardDto(userForwardFrom);
        messageDto.setUserRate(messageService.getUserRateOnMessage(userId, message.getId()));
        messageDto.setAverageRate(messageService.getAverageRate(message.getId()));
        return messageDto;
    }

    private Set<Long> getReceivers(Chat chat) {
        Set<Long> receivers;

        if (chat instanceof PrivateChat privateChat) {
            receivers = new HashSet<>(List.of(privateChat.getUser1().getId(), privateChat.getUser2().getId()));

        } else if (chat instanceof PublicChat publicChat) {
            receivers = publicChatService.getChatMembers(publicChat.getId()).stream()
                    .map(User::getId)
                    .collect(Collectors.toSet());
        } else {
            throw new NotImplementedException();
        }

        return receivers;
    }
}
