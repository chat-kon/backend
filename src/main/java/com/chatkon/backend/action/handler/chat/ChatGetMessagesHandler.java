package com.chatkon.backend.action.handler.chat;

import com.chatkon.backend.action.Action;
import com.chatkon.backend.action.ActionResult;
import com.chatkon.backend.action.ActionType;
import com.chatkon.backend.action.handler.ActionHandler;
import com.chatkon.backend.model.dto.chat.ChatGetMessagesRequestDto;
import com.chatkon.backend.model.dto.chat.ChatGetMessagesResponseDto;
import com.chatkon.backend.model.dto.message.MessageDto;
import com.chatkon.backend.model.dto.user.UserDto;
import com.chatkon.backend.model.entity.message.MessageRate;
import com.chatkon.backend.service.chat.ChatService;
import com.chatkon.backend.service.message.MessageService;
import com.chatkon.backend.util.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class ChatGetMessagesHandler implements ActionHandler<ChatGetMessagesRequestDto> {
    private final ChatService chatService;
    private final MessageService messageService;

    @Override
    public ActionType type() {
        return ActionType.GET_CHAT_MESSAGES;
    }

    @Override
    public ActionResult handle(Long userId, ChatGetMessagesRequestDto dto) {

        Set<MessageDto> messages = new HashSet<>();
        Set<UserDto> users = new HashSet<>();

        chatService.getChatMessages(userId, dto.getChatId()).forEach(message -> {
            MessageDto messageDto = Mapper.map(message, MessageDto.class);
            messageDto.setSenderId(message.getSender().getId());
            messageDto.setChatId(message.getChat().getId());
            messageDto.setAverageRate(messageService.getAverageRate(messageDto.getId()));
            var userRate = messageService.getUserRateOnMessage(userId, messageDto.getId());
            if (userRate != null) messageDto.setUserRate(userRate);
            messageDto.setViews(messageService.getMessageViewCount(messageDto.getId()));
            UserDto userDto = Mapper.map(message.getSender(), UserDto.class);
            messages.add(messageDto);
            users.add(userDto);
        });

        ChatGetMessagesResponseDto responseDto = ChatGetMessagesResponseDto.builder()
                .users(users)
                .messages(messages)
                .build();

        Action action = Action.builder()
                .type(ActionType.GET_CHAT_MESSAGES)
                .dto(responseDto)
                .build();

        return ActionResult.builder()
                .action(action)
                .receivers(Set.of(userId))
                .build();
    }
}