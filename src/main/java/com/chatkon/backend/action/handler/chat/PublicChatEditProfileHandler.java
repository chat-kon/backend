package com.chatkon.backend.action.handler.chat;

import com.chatkon.backend.action.Action;
import com.chatkon.backend.action.ActionResult;
import com.chatkon.backend.action.ActionType;
import com.chatkon.backend.action.handler.ActionHandler;
import com.chatkon.backend.model.dto.chat.ChatDto;
import com.chatkon.backend.model.dto.chat.PublicChatEditProfileRequestDto;
import com.chatkon.backend.model.dto.chat.PublicChatEditProfileResponseDto;
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
public class PublicChatEditProfileHandler implements ActionHandler<PublicChatEditProfileRequestDto> {
    private final PublicChatService publicChatService;

    @Override
    public ActionType type() {
        return ActionType.EDIT_PROFILE_PUBLIC_CHAT;
    }

    @Override
    public ActionResult handle(Long userId, PublicChatEditProfileRequestDto dto) {

        PublicChat chat = Mapper.map(dto.getChat(), PublicChat.class);

        PublicChat editedChat = publicChatService.editProfilePublicChat(chat, userId);

        PublicChatEditProfileResponseDto responseDto = PublicChatEditProfileResponseDto.builder()
                .chat(Mapper.map(editedChat, ChatDto.class))
                .build();

        Action action = Action.builder()
                .type(ActionType.EDIT_PROFILE_PUBLIC_CHAT)
                .dto(responseDto).build();

        Set<Long> receivers = publicChatService.getChatMembers(editedChat.getId()).stream()
                .map(User::getId)
                .collect(Collectors.toSet());

        return ActionResult.builder()
                .action(action)
                .receivers(receivers).build();
    }

}
