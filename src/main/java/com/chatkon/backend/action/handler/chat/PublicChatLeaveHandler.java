package com.chatkon.backend.action.handler.chat;

import com.chatkon.backend.action.Action;
import com.chatkon.backend.action.ActionResult;
import com.chatkon.backend.action.ActionType;
import com.chatkon.backend.action.handler.ActionHandler;
import com.chatkon.backend.model.dto.chat.PublicChatLeaveRequestDto;
import com.chatkon.backend.model.dto.chat.PublicChatLeaveResponseDto;
import com.chatkon.backend.model.dto.user.UserDto;
import com.chatkon.backend.model.entity.chat.PublicChat;
import com.chatkon.backend.model.entity.user.User;
import com.chatkon.backend.service.chat.ChatService;
import com.chatkon.backend.service.chat.PublicChatService;
import com.chatkon.backend.service.user.UserService;
import com.chatkon.backend.util.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PublicChatLeaveHandler implements ActionHandler<PublicChatLeaveRequestDto> {
    private final PublicChatService publicChatService;
    private final UserService userService;
    private final ChatService chatService;

    @Override
    public ActionType type() {
        return ActionType.LEAVE_PUBLIC_CHAT;
    }

    @Override
    public ActionResult handle(Long userId, PublicChatLeaveRequestDto dto) {
        Set<Long> receivers = publicChatService.getChatMembers(dto.getChatId()).stream()
                .map(User::getId).collect(Collectors.toSet());

        PublicChat chat = publicChatService.findPublicChat(dto.getChatId());

        if (chat.getOwner().getId().equals(userId)) {
            chatService.deleteChat(userId, dto.getChatId());
        } else {
            publicChatService.leavePublicChat(dto.getChatId(), userId);
        }

        PublicChatLeaveResponseDto responseDto = PublicChatLeaveResponseDto.builder()
                .chatId(chat.getId())
                .user(Mapper.map(userService.findUser(userId), UserDto.class))
                .build();

        Action action = Action.builder()
                .type(ActionType.LEAVE_PUBLIC_CHAT)
                .dto(responseDto).build();

        return ActionResult.builder()
                .action(action)
                .receivers(receivers)
                .build();
    }
}
