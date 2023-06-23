package com.chatkon.backend.action.handler.chat;

import com.chatkon.backend.action.Action;
import com.chatkon.backend.action.ActionResult;
import com.chatkon.backend.action.ActionType;
import com.chatkon.backend.action.handler.ActionHandler;
import com.chatkon.backend.model.dto.chat.ChatDto;
import com.chatkon.backend.model.dto.chat.PublicChatCreateRequestDto;
import com.chatkon.backend.model.dto.chat.PublicChatCreateResponseDto;
import com.chatkon.backend.model.entity.chat.PublicChat;
import com.chatkon.backend.model.entity.user.User;
import com.chatkon.backend.service.chat.PublicChatService;
import com.chatkon.backend.util.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PublicChatCreateHandler implements ActionHandler<PublicChatCreateRequestDto> {
    private final PublicChatService publicChatService;

    @Override
    public ActionType type() {
        return ActionType.CREATE_PUBLIC_CHAT;
    }

    @Override
    public ActionResult handle(Long userId, PublicChatCreateRequestDto dto) {
        PublicChat chat = Mapper.map(dto.getChat(), PublicChat.class);

        PublicChat createdChat = publicChatService.createPublicChat(userId, chat, dto.getInitMemberIds());

        PublicChatCreateResponseDto responseDto = PublicChatCreateResponseDto.builder()
                .chat(Mapper.map(createdChat, ChatDto.class))
                .build();

        Action action = Action.builder()
                .type(ActionType.CREATE_PUBLIC_CHAT)
                .dto(responseDto).build();

        Set<Long> receivers = publicChatService.getChatMembers(createdChat.getId()).stream()
                .map(User::getId).collect(Collectors.toSet());

        return ActionResult.builder()
                .action(action)
                .receivers(receivers).build();
    }
}
